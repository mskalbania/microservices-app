package edu.application.licenses.cache;

import edu.application.licenses.events.OrganizationChangeChannel;
import edu.application.licenses.events.model.OrganizationChange;
import edu.application.licenses.model.Organization;
import io.vavr.control.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
@EnableBinding(OrganizationChangeChannel.class)
public class OrganizationCache { //Just for example purposes - not ok for distributed systems

    private static final Logger LOG = LoggerFactory.getLogger(OrganizationCache.class);

    private Map<String, Organization> organizationCache = Collections.synchronizedMap(new HashMap<>());

    public OrganizationCache() {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(this::invalidate, 0, 15, TimeUnit.MINUTES);
    }

    @StreamListener("inboundOrganizationChanges")
    public void onOrganizationChange(OrganizationChange organizationChange) {
        LOG.info("Organization change event intercepted data - {}", organizationChange);
        organizationCache.remove(organizationChange.getOrganizationId());
    }

    public void put(Organization organization) {
        LOG.info("Added new record to cache: {}", organization);
        organizationCache.put(organization.getId(), organization);
    }

    public Option<Organization> get(String organizationId) {
        return Option.of(organizationCache.get(organizationId));
    }

    public void invalidate() {
        LOG.info("Cache invalidated.");
        organizationCache.clear();
    }
}
