package edu.application.licenses.controller;

import edu.application.licenses.client.OrganizationFeignClient;
import edu.application.licenses.model.License;
import edu.application.licenses.reposiotry.LicensesRepository;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.vavr.collection.List.ofAll;

@RestController
@RequestMapping("/v1/organizations/{organizationId}/licenses")
public class LicensesController {

    private static final Logger LOG = LoggerFactory.getLogger(LicensesController.class);

    //Might move to LicenseServiceClass
    private final LicensesRepository licensesRepository;
    private final OrganizationFeignClient organizationClient;

    public LicensesController(LicensesRepository licensesRepository, OrganizationFeignClient organizationClient) {
        this.licensesRepository = licensesRepository;
        this.organizationClient = organizationClient;
    }

    @GetMapping
    public ResponseEntity<List<License>> getAll(@PathVariable("organizationId") String organizationId) {
        return ResponseEntity.ok(ofAll(licensesRepository.findAllByOrganizationId(organizationId)));
    }

    @GetMapping("/{licenseId}")
    public ResponseEntity<License> getSpecific(@PathVariable("organizationId") String organizationId,
                                               @PathVariable("licenseId") String licenseId) {
        return licensesRepository.findByOrganizationIdAndId(organizationId, licenseId)
                                 .map(license -> fillInOrganizationInfo(organizationId, license))
                                 .map(ResponseEntity::ok)
                                 .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private License fillInOrganizationInfo(String organizationId, License license) {
        return Try.of(() -> organizationClient.getOrganization(organizationId))
                  .onFailure(ex -> LOG.error("Unable to retrieve organization data"))
                  .map(license::withOrganizationInfo)
                  .getOrElse(license);
    }
}
