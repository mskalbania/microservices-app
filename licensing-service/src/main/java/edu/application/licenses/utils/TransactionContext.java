package edu.application.licenses.utils;

import lombok.Data;

@Data
public class TransactionContext {

    public static final String CONVERSATION_ID_HEADER = "conv-id";

    private String conversationId;
}
