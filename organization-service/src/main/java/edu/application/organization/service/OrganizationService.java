package edu.application.organization.service;

import edu.application.organization.events.EventPublisher;
import edu.application.organization.exception.OrganizationNotFoundException;
import edu.application.organization.exception.OrganizationServiceException;
import edu.application.organization.model.Organization;
import edu.application.organization.repository.OrganizationRepository;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrganizationService {

    private static final Logger LOG = LoggerFactory.getLogger(OrganizationService.class);

    private final OrganizationRepository organizationRepository;
    private final EventPublisher publisher;

    public OrganizationService(OrganizationRepository organizationRepository, EventPublisher publisher) {
        this.organizationRepository = organizationRepository;
        this.publisher = publisher;
    }

    public Organization getById(String organizationId) {
        return organizationRepository.findById(organizationId)
                                     .orElseThrow(() -> new OrganizationNotFoundException(organizationId));
    }

    public List<Organization> getAll() {
        return List.ofAll(organizationRepository.findAll());
    }

    public Organization save(Organization organization) {
        return Try.of(() -> organizationRepository.save(organization))
                  .onSuccess(org -> publisher.publishOrganizationChangeEvent("SAVE/UPDATE", org.getId()))
                  .onFailure(ex -> LOG.error("Unable to save/update organization: [{}]", ex.getMessage()))
                  .getOrElseThrow(ex -> new OrganizationServiceException(ex.getMessage()));
    }

    public void delete(String organizationId) {
        Try.run(() -> organizationRepository.deleteById(organizationId))
           .onSuccess(__ -> publisher.publishOrganizationChangeEvent("DELETE", organizationId))
           .onFailure(ex -> LOG.error("Unable to delete organization: [{}]", ex.getMessage()))
           .getOrElseThrow(() -> new OrganizationNotFoundException(organizationId));
    }
}
