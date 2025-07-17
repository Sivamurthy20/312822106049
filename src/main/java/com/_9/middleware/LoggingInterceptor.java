package com._9.middleware;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoggingInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        logger.info("Incoming request: {} {} from {}", request.getMethod(), request.getRequestURI(), request.getRemoteAddr());
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, @Nullable Exception ex) {
        logger.info("Completed request: {} {} with status {}", request.getMethod(), request.getRequestURI(), response.getStatus());
        if (ex != null) {
            logger.error("Exception: ", ex);
        }
    }
}
