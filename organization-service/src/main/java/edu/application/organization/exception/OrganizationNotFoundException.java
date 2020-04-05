package edu.application.organization.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrganizationNotFoundException extends RuntimeException {

    private static final String ORGANIZATION_NOT_FOUND_MESSAGE = "Organization - %s was not found";
    private final String message;

    public OrganizationNotFoundException(String organizationId) {
        this.message = String.format(ORGANIZATION_NOT_FOUND_MESSAGE, organizationId);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
