package com.evo.common.webapp.config;

import com.evo.common.webapp.support.CachedHttpServletRequestWrapper;
import com.evo.common.webapp.support.SecurityUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@WebFilter("/api/**")
@Slf4j
@Order(100)
public class ActionLogFilter extends OncePerRequestFilter {
    private static final int START_LOG_HTTP_STATUS = HttpStatus.BAD_REQUEST.value();
    private static final String LOG_REMOTE_IP = "remote_ip";
    private static final String LOG_USERNAME = "username";
    private static final String LOG_CLIENT_MESSAGE_ID = "client_message_id";

    private final List<String> blackList =
            List.of(
                    "\\/api\\/certificate\\/.well-known\\/jwks\\.json",
                    ".*\\/actuator\\/.*",
                    "\\/api\\/*-logs.*",
                    "/swagger-ui.*",
                    "/swagger-resources.*",
                    "/v2/api-docs.*",
                    ".*\\/integrations\\/files\\/upload");
    private final List<String> blackListMimeType =
            List.of("multipart\\/form-data.*", "image\\/.*", "application\\/octet-stream.*");

    @Override
    protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        Instant start = Instant.now();
        HttpServletRequest httpServletRequest = servletRequest;
        ContentCachingResponseWrapper cachedResponse =
                new ContentCachingResponseWrapper(servletResponse);
        cachedResponse.setCharacterEncoding("UTF-8");

        String requestContentType = httpServletRequest.getHeader("Content-Type");
        boolean ignoredRequestBody =
                Objects.nonNull(requestContentType)
                        && this.blackListMimeType.stream().anyMatch(requestContentType::matches);

        if (!ignoredRequestBody) {
            httpServletRequest = new CachedHttpServletRequestWrapper(servletRequest);
        }
        String clientMessageId = httpServletRequest.getHeader(LOG_CLIENT_MESSAGE_ID);
        String remoteIp = this.getRemoteIp(httpServletRequest);
        RequestAttributes request = RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(request)) {
            request.setAttribute(LOG_CLIENT_MESSAGE_ID, clientMessageId, RequestAttributes.SCOPE_REQUEST);
            request.setAttribute(LOG_REMOTE_IP, remoteIp, RequestAttributes.SCOPE_REQUEST);
        }
        try {
            MDC.put(LOG_REMOTE_IP, remoteIp);
            MDC.put(
                    LOG_USERNAME,
                    SecurityUtils.getCurrentUser().orElse("anonymous"));
            MDC.put(LOG_CLIENT_MESSAGE_ID, clientMessageId);
            filterChain.doFilter(httpServletRequest, cachedResponse);
        } finally {
            Instant finishRequest = Instant.now();
            StringBuilder responseStr = new StringBuilder();
            byte[] responseArray = cachedResponse.getContentAsByteArray();
            cachedResponse.copyBodyToResponse();
            // check response content type
            String responseContentType = cachedResponse.getHeader("Content-Type");
            boolean ignoredResponse =
                    Objects.nonNull(responseContentType)
                            && this.blackListMimeType.stream()
                            .anyMatch(responseContentType::matches);
            if (!ignoredResponse) {
                responseStr
                        .append("Response: ")
                        .append(new String(responseArray, cachedResponse.getCharacterEncoding()));
            }
            log.info("ActionLogFilter response: " + responseStr.toString());
            if (shouldFilter(servletRequest)
                    && cachedResponse.getStatus() >= START_LOG_HTTP_STATUS) {
                long time = Duration.between(start, finishRequest).toMillis();

            }
        }
        MDC.remove(LOG_REMOTE_IP);
        MDC.remove(LOG_USERNAME);
        MDC.remove(LOG_CLIENT_MESSAGE_ID);
    }

    private boolean shouldFilter(HttpServletRequest request) {
        if (this.blackList.isEmpty()) {
            return true;
        }
        String uri = String.valueOf(request.getRequestURI());
        return this.blackList.stream().noneMatch(uri::matches);
    }

    private String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasLength(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            int index = ip.indexOf(",");
            if (index != -1) {
                log.info("get remote ip: {}", ip);
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.hasLength(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    private String replaceRequestBody(String body) {
        if (!StringUtils.hasLength(body)) {
            return body;
        }
        return body.replaceAll("\"password\":\"(.*?)\"", "\"password\":\"******\"")
                .replaceAll("\"clientSecret\":\"(.*?)\"", "\"clientSecret\":\"******\"");
    }
}
