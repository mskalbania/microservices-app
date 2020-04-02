package edu.application.licenses.client;

import edu.application.licenses.model.Organization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class OrganizationClient {

    private static final Logger LOG = LoggerFactory.getLogger(OrganizationClient.class);

    private final RestTemplate restTemplate;

    public OrganizationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<Organization> getOrganization(String organizationId) {
        ResponseEntity<Organization> exchange = restTemplate.exchange("http://organizationservice/v1/organizations/{organizationId}",
                                                                      HttpMethod.GET, null, Organization.class, organizationId);
        LOG.info("RS: " + exchange);
        return Optional.ofNullable(exchange.getBody());
    }

}
