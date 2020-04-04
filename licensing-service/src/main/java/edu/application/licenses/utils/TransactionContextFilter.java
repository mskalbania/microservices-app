package edu.application.licenses.utils;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class TransactionContextFilter implements Filter {

    private static final String CONVERSATION_ID_HEADER = "conv-id";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest rq = (HttpServletRequest) servletRequest;
        String conversationId = rq.getHeader(CONVERSATION_ID_HEADER);
        if (conversationId != null) {
            TransactionContextHolder.getCtx()
                                    .setConversationId(conversationId);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
