package com.audition.configuration;

import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.extension.incubator.web.HttpServerResponseDecorator;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class ResponseHeaderInjector implements HttpServerResponseDecorator {

    @Override
    public HttpServletResponse decorate(HttpServletResponse response, Context context) {
        try (Scope scope = context.makeCurrent()) {
            response.addHeader("X-Trace-Id", context.getTraceIdAsHex());
            response.addHeader("X-Span-Id", context.getSpanIdAsHex());
        }
        return response;
    }

}
