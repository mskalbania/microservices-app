package edu.application.organization.controller;

import edu.application.organization.model.Organization;
import edu.application.organization.repository.OrganizationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/organizations")
public class OrganizationController {

    private final OrganizationRepository organizationRepository;

    public OrganizationController(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @GetMapping("/{organizationId}")
    public ResponseEntity<Organization> getSpecific(@PathVariable("organizationId") String organizationId) {
        return organizationRepository.findById(organizationId)
                                     .map(ResponseEntity::ok)
                                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Iterable<Organization>> getAll() {
        return ResponseEntity.ok(organizationRepository.findAll());
    }
}
