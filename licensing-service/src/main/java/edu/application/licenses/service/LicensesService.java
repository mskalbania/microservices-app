package edu.application.licenses.service;

import edu.application.licenses.client.OrganizationClient;
import edu.application.licenses.exception.LicensesServiceException;
import edu.application.licenses.model.License;
import edu.application.licenses.reposiotry.LicensesRepository;
import edu.application.licenses.utils.TransactionContextHolder;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller
public class LicensesService {

    private static final Logger LOG = LoggerFactory.getLogger(LicensesService.class);

    private final LicensesRepository licensesRepository;
    private final OrganizationClient organizationClient;

    public LicensesService(LicensesRepository licensesRepository,
                           OrganizationClient organizationClient) {
        this.licensesRepository = licensesRepository;
        this.organizationClient = organizationClient;
    }

    public List<License> findAllByOrganizationId(String organizationId) {
        return List.ofAll(licensesRepository.findAllByOrganizationId(organizationId));
    }

    public Option<License> findByOrganizationIdAndId(String organizationId, String licenseId) {
        return Option.ofOptional(licensesRepository.findByOrganizationIdAndId(organizationId, licenseId))
                     .map(license -> fillInOrganizationInfo(organizationId, license));
    }

    private License fillInOrganizationInfo(String organizationId, License license) {
        return Try.of(() -> organizationClient.getOrganization(organizationId))
                  .map(license::withOrganizationInfo)
                  .peek(__ -> LOG.info("Conv-id [{}]", TransactionContextHolder.getCtx().getConversationId()))
                  .onFailure(ex -> LOG.error("Unable to retrieve organization info, cause: [{}], [{}]", ex.getMessage(), ex.getStackTrace()))
                  .getOrElseThrow(LicensesServiceException::unableToRetrieveOrganizationData);
    }
}
