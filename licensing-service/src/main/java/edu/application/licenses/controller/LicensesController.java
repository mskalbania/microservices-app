package edu.application.licenses.controller;

import edu.application.licenses.reposiotry.LicensesRepository;
import edu.application.licenses.model.License;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/organizations/{organizationId}/licenses")
public class LicensesController {

    private final LicensesRepository licensesRepository;

    public LicensesController(LicensesRepository licensesRepository) {
        this.licensesRepository = licensesRepository;
    }

    @GetMapping
    public ResponseEntity<List<License>> getAll(@PathVariable("organizationId") String organizationId) {
        return ResponseEntity.ok(licensesRepository.findAllByOrganizationId(organizationId));
    }

    @GetMapping("/{licenseId}")
    public ResponseEntity<License> getSpecific(@PathVariable("organizationId") String organizationId,
                                               @PathVariable("licenseId") String licenseId) {
        return licensesRepository.findByOrganizationIdAndId(organizationId, licenseId)
                                 .map(ResponseEntity::ok)
                                 .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
