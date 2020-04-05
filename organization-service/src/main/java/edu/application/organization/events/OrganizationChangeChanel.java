package edu.application.organization.events;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface OrganizationChangeChanel {

    @Output("outboundOrganizationChanges")
    MessageChannel organizationChange();
}
