package edu.application.organization.controller;

import edu.application.organization.model.Organization;
import edu.application.organization.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/organizations")
public class OrganizationController {

    private final OrganizationRepository organizationRepository;
    private final boolean isMisbehavedService;

    public OrganizationController(OrganizationRepository organizationRepository,
                                  @Value("${application.misbehavingInstance}") boolean isMisbehavedService) {
        this.organizationRepository = organizationRepository;
        this.isMisbehavedService = isMisbehavedService;
    }

    @GetMapping("/{organizationId}")
    public ResponseEntity<Organization> getSpecific(@PathVariable("organizationId") String organizationId) {
        if (isMisbehavedService) {
            delay();
        }
        return organizationRepository.findById(organizationId)
                                     .map(ResponseEntity::ok)
                                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Iterable<Organization>> getAll() {
        return ResponseEntity.ok(organizationRepository.findAll());
    }

    private void delay() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignored) {

        }
    }
}
