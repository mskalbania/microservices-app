package edu.application.licenses.controller;

import edu.application.licenses.model.License;
import edu.application.licenses.service.LicensesService;
import io.vavr.collection.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.vavr.collection.List.ofAll;

@RestController
@RequestMapping("/v1/organizations/{organizationId}/licenses")
public class LicensesController {

    private final LicensesService licensesService;

    public LicensesController(LicensesService licensesService) {
        this.licensesService = licensesService;
    }

    @GetMapping
    public ResponseEntity<List<License>> getAll(@PathVariable("organizationId") String organizationId) {
        return ResponseEntity.ok(ofAll(licensesService.findAllByOrganizationId(organizationId)));
    }

    @GetMapping("/{licenseId}")
    public ResponseEntity<License> getSpecific(@PathVariable("organizationId") String organizationId,
                                               @PathVariable("licenseId") String licenseId) {
        return licensesService.findByOrganizationIdAndId(organizationId, licenseId)
                              .map(ResponseEntity::ok)
                              .getOrElse(() -> ResponseEntity.notFound().build());
    }
}
