package edu.application.licenses.utils;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

import static edu.application.licenses.utils.TransactionContext.CONVERSATION_ID_HEADER;

public class TransactionContextInterceptor implements ClientHttpRequestInterceptor {

    @Override //Used to pass conv id to downlines
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().add(CONVERSATION_ID_HEADER, TransactionContextHolder.getCtx().getConversationId());
        return execution.execute(request, body);
    }
}
