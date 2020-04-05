package edu.application.organization.utils;

import lombok.Data;

@Data
public class TransactionContext {

    public static final String CONVERSATION_ID_HEADER = "conv-id";

    private String conversationId;
}
