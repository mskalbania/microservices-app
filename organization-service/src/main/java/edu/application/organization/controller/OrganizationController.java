package edu.application.organization.controller;

import edu.application.organization.model.Organization;
import edu.application.organization.repository.OrganizationRepository;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/organizations")
public class OrganizationController {

    private final OrganizationRepository organizationRepository;
    private final boolean isMisbehavedService;

    private static final Logger LOG = LoggerFactory.getLogger(OrganizationController.class);

    public OrganizationController(OrganizationRepository organizationRepository,
                                  @Value("${application.misbehavingInstance}") boolean isMisbehavedService) {
        this.organizationRepository = organizationRepository;
        this.isMisbehavedService = isMisbehavedService;
    }

    @GetMapping("/{organizationId}")
    public ResponseEntity<Organization> getById(@PathVariable("organizationId") String organizationId,
                                                @RequestHeader("conv-id") String convId) {

        LOG.info("ConversationId [{}]", convId); //Testing if upline forwarded conv id
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

    @PostMapping
    public ResponseEntity<Organization> add(@RequestBody Organization organization) {
        return Try.of(() -> organizationRepository.save(organization))
                  .onFailure(ex -> LOG.error("Unable to store organization: [{}]", ex.getMessage()))
                  .map(org -> ResponseEntity.status(204).body(org))
                  .getOrElse(() -> ResponseEntity.badRequest().build());
    }


    private void delay() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignored) {

        }
    }
}
