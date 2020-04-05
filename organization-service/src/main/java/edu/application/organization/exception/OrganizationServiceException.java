package edu.application.organization.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ResponseStatus(INTERNAL_SERVER_ERROR)
public class OrganizationServiceException extends RuntimeException {

    public OrganizationServiceException(String message) {
        super(message);
    }
}
