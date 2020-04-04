package edu.application.licenses.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class LicensesServiceException extends RuntimeException {

    public static LicensesServiceException unableToRetrieveOrganizationData() {
        return new LicensesServiceException("Unable to retrieve organization data");
    }

    public LicensesServiceException(String message) {
        super(message);
    }
}