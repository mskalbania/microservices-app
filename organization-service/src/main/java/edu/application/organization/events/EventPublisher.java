package edu.application.organization.events;

import edu.application.organization.events.model.OrganizationChange;
import edu.application.organization.utils.TransactionContextHolder;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(OrganizationChangeChanel.class)
public class EventPublisher {

    private final OrganizationChangeChanel chanel;

    public EventPublisher(OrganizationChangeChanel chanel) {
        this.chanel = chanel;
    }

    public void publishOrganizationChangeEvent(String action, String organizationId) {
        OrganizationChange change = new OrganizationChange(OrganizationChange.class.getTypeName(),
                                                           action, organizationId,
                                                           TransactionContextHolder.getCtx().getConversationId());
        chanel.organizationChange()
              .send(MessageBuilder.withPayload(change).build());
    }
}
