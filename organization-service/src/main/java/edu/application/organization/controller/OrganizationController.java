package edu.application.organization.controller;

import edu.application.organization.model.Organization;
import edu.application.organization.service.OrganizationService;
import io.vavr.collection.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/v1/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;
    private final boolean isMisbehavedService;

    private static final Logger LOG = LoggerFactory.getLogger(OrganizationController.class);

    public OrganizationController(OrganizationService organizationService,
                                  @Value("${application.misbehavingInstance}") boolean isMisbehavedService) {
        this.organizationService = organizationService;
        this.isMisbehavedService = isMisbehavedService;
    }

    @GetMapping("/{organizationId}")
    public ResponseEntity<Organization> getById(@PathVariable("organizationId") String organizationId,
                                                @RequestHeader("conv-id") String convId) {

        LOG.info("ConversationId [{}]", convId); //Testing if upline service forwarded conv id
        if (isMisbehavedService) {
            delay();
        }
        return ResponseEntity.ok(organizationService.getById(organizationId));
    }

    @GetMapping
    public ResponseEntity<List<Organization>> getAll() {
        return ResponseEntity.ok(organizationService.getAll());
    }

    @PostMapping
    public ResponseEntity<Organization> add(@RequestBody Organization organization) {
        return ResponseEntity.status(CREATED).body(organizationService.save(organization));
    }

    @DeleteMapping("/{organizationId}")
    public ResponseEntity<?> delete(@PathVariable("organizationId") String organizationId) {
        organizationService.delete(organizationId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{organizationId}")
    public ResponseEntity<?> update(@PathVariable("organizationId") String organizationId,
                                    @RequestBody Organization organization) {
        return ResponseEntity.ok(organizationService.save(organization));
    }

    private void delay() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignored) {

        }
    }
}
