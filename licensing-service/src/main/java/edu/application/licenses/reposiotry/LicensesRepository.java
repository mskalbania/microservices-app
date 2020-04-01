package edu.application.licenses.reposiotry;

import edu.application.licenses.model.License;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface LicensesRepository extends CrudRepository<License, String> {

    List<License> findAllByOrganizationId(String organizationId);

    Optional<License> findByOrganizationIdAndId(String organizationId, String licenseId);
}
