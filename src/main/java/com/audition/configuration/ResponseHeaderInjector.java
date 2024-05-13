package com.audition.configuration;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

@Aspect
@Component
public class ResponseHeaderInjector {

    private final Tracer tracer;

    public ResponseHeaderInjector(Tracer tracer) {
        this.tracer = tracer;
    }

    @Around("execution(* com.audition.web.*.*(..))")
    public Object injectResponseHeaders(ProceedingJoinPoint joinPoint) throws Throwable {
        Span span = tracer.getCurrentSpan();
        HttpServletResponse response = getResponse(joinPoint.getArgs());
        if (span != null && response != null) {
            response.setHeader("X-Trace-Id", span.getSpanContext().getTraceId());
            response.setHeader("X-Span-Id", span.getSpanContext().getSpanId());
        }
        return joinPoint.proceed();
    }

    private HttpServletResponse getResponse(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof HttpServletResponse) {
                return (HttpServletResponse) arg;
            }
        }
        return null;
    }
}
