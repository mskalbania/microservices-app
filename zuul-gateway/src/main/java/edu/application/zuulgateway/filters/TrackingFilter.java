package edu.application.zuulgateway.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TrackingFilter extends ZuulFilter {

    public static final String CONVERSATION_ID = "conv-id";

    private static final String PRE_FILTER_TYPE = "pre";
    private static final int FILTERING_ORDER = 1;
    private static final boolean FILTERING_ENABLED = true;

    private static final Logger LOG = LoggerFactory.getLogger(TrackingFilter.class);

    @Override
    public String filterType() {
        return PRE_FILTER_TYPE;
    }

    @Override
    public int filterOrder() {
        return FILTERING_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return FILTERING_ENABLED;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        LOG.info("Incoming request: {} @ {}", ctx.getRequest(), ctx.getRequest().getRequestURI());
        ctx.addZuulRequestHeader(CONVERSATION_ID, UUID.randomUUID().toString());
        return null;
    }
}
