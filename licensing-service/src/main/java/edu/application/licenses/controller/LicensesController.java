package edu.application.licenses.controller;

import edu.application.licenses.client.OrganizationClient;
import edu.application.licenses.model.License;
import edu.application.licenses.model.Organization;
import edu.application.licenses.reposiotry.LicensesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/organizations/{organizationId}/licenses")
public class LicensesController {

    private static final Logger LOG = LoggerFactory.getLogger(LicensesController.class);

    //Might move to LicenseServiceClass
    private final LicensesRepository licensesRepository;
    private final OrganizationClient organizationClient;

    public LicensesController(LicensesRepository licensesRepository, OrganizationClient organizationClient) {
        this.licensesRepository = licensesRepository;
        this.organizationClient = organizationClient;
    }

    @GetMapping
    public ResponseEntity<List<License>> getAll(@PathVariable("organizationId") String organizationId) {
        return ResponseEntity.ok(licensesRepository.findAllByOrganizationId(organizationId));
    }

    @GetMapping("/{licenseId}")
    public ResponseEntity<License> getSpecific(@PathVariable("organizationId") String organizationId,
                                               @PathVariable("licenseId") String licenseId) {

        Optional<Organization> organization = organizationClient.getOrganization(organizationId);

        return licensesRepository.findByOrganizationIdAndId(organizationId, licenseId)
                                 .map(license -> fillInOrganizationInfo(organizationId, license))
                                 .map(ResponseEntity::ok)
                                 .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private License fillInOrganizationInfo(String organizationId, License license) {
        return organizationClient.getOrganization(organizationId)
                                 .map(org -> {
                                     //FIXME MULTILINE LAMBDA & IMMUTABLE
                                     license.setOrganizationName(org.getName());
                                     license.setContactName(org.getContactName());
                                     license.setContactEmail(org.getContactEmail());
                                     license.setContactPhone(org.getContactPhone());
                                     return license;
                                 }).orElse(license);
    }
}
