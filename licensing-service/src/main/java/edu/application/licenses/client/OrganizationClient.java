package edu.application.licenses.client;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import edu.application.licenses.cache.OrganizationCache;
import edu.application.licenses.exception.LicensesServiceException;
import edu.application.licenses.model.Organization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static com.netflix.hystrix.contrib.javanica.annotation.HystrixException.RUNTIME_EXCEPTION;

@Component
public class OrganizationClient {

    private static final Logger LOG = LoggerFactory.getLogger(OrganizationClient.class);

    private final RestTemplate restTemplate;
    private final OrganizationCache organizationCache;

    public OrganizationClient(RestTemplate restTemplate, OrganizationCache organizationCache) {
        this.restTemplate = restTemplate;
        this.organizationCache = organizationCache;
    }

    @HystrixCommand(fallbackMethod = "getFromCache",
                    threadPoolKey = "organizationResourceThreadPool",
                    raiseHystrixExceptions = RUNTIME_EXCEPTION,
                    commandProperties = {
                            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000"),
                            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2"), //pick 2 rq in window
                            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"), //check if 50% of them failed
                            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "60000") //wait 60s until next rq is passed to remote
                    },
                    threadPoolProperties = {
                            @HystrixProperty(name = "coreSize", value = "10"), //Should reject >10 rq in same time
                            @HystrixProperty(name = "maxQueueSize", value = "10")} //10 rq will be queued if pool is busy
                    )
    public Organization getOrganization(String organizationId) {
        ResponseEntity<Organization> exchange = restTemplate.exchange("http://organizationservice/v1/organizations/{organizationId}",
                                                                      HttpMethod.GET, null, Organization.class, organizationId);
        if (exchange.getStatusCode().isError()) {
            LOG.error("Organization downline returned error status code: {}, body:{}  ", exchange.getStatusCodeValue(), exchange.getBody());
            return getFromCache(organizationId);
        } else {
            Organization organization = exchange.getBody();
            organizationCache.put(organization);
            return organization;
        }
    }

    private Organization getFromCache(String organizationId) {
        LOG.warn("Organization cache fallback triggered...");
        return organizationCache.get(organizationId)
                                .getOrElseThrow(LicensesServiceException::unableToRetrieveOrganizationData);
    }
}
