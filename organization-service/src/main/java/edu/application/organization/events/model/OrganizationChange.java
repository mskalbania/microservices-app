package edu.application.organization.events.model;

import lombok.Data;

@Data
public class OrganizationChange {

    private final String type;
    private final String action;
    private final String organizationId;
    private final String conversationId;
}
