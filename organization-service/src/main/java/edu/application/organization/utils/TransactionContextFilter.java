package edu.application.organization.utils;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static edu.application.organization.utils.TransactionContext.CONVERSATION_ID_HEADER;

@Component
public class TransactionContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest rq = (HttpServletRequest) servletRequest;
        String conversationId = rq.getHeader(CONVERSATION_ID_HEADER);
        if (conversationId != null) {
            TransactionContextHolder.getCtx()
                                    .setConversationId(conversationId);
        }
        HttpServletResponse rs = (HttpServletResponse) servletResponse;
        rs.addHeader(CONVERSATION_ID_HEADER, TransactionContextHolder.getCtx().getConversationId());
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
