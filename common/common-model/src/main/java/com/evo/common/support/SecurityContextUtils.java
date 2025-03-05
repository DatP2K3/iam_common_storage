package com.evo.common.support;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SecurityContextUtils {
    private static final String UNKNOWN = "Unknown";

    public static Map<String, Object> getSecurityContextMap() {
        Map<String, Object> contextMap = new HashMap<>();

        contextMap.put("time", getCurrentUtcTime());
        contextMap.put("username", getCurrentUsername());

        // Try to get IP from current request if available
        HttpServletRequest request = getCurrentRequest();
        if (request != null) {
            contextMap.put("ipAddress", getClientIpAddress(request));
            contextMap.put("device", getDeviceInfo(request));
            contextMap.put("location", UNKNOWN); // Would need geo IP service
        } else {
            contextMap.put("ipAddress", UNKNOWN);
            contextMap.put("device", UNKNOWN);
            contextMap.put("location", UNKNOWN);
        }

        return contextMap;
    }

    public static String getCurrentUtcTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(new Date());
    }

    public static String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null
                && auth.isAuthenticated()
                && !auth.getPrincipal().toString().equals("anonymousUser")) {
            return auth.getName();
        }
        return UNKNOWN;
    }

    private static HttpServletRequest getCurrentRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        } catch (Exception e) {
            return null;
        }
    }

    private static String getClientIpAddress(HttpServletRequest request) {
        // Kiểm tra các header thông dụng theo thứ tự ưu tiên
        String[] headerNames = {
            "X-Forwarded-For",
            "HTTP_X_FORWARDED_FOR",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED",
            "X-Real-IP",
            "X-FORWARDED-FOR"
        };

        for (String headerName : headerNames) {
            String header = request.getHeader(headerName);
            if (header != null && !header.isEmpty() && !"unknown".equalsIgnoreCase(header)) {
                // X-Forwarded-For có thể chứa nhiều IP, lấy IP đầu tiên (client gốc)
                if (header.contains(",")) {
                    return header.split(",")[0].trim();
                }
                return header.trim();
            }
        }

        // Fallback to remote address
        String remoteAddr = request.getRemoteAddr();

        // Xử lý trường hợp IPv6 localhost
        if ("0:0:0:0:0:0:0:1".equals(remoteAddr)) {
            // Trả về IPv4 localhost thay vì IPv6
            return "127.0.0.1";
        }

        return remoteAddr;
    }

    private static String getDeviceInfo(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null || userAgent.isEmpty()) {
            return UNKNOWN;
        }

        StringBuilder deviceInfo = new StringBuilder();

        // Xác định trình duyệt
        if (userAgent.contains("Chrome") && userAgent.contains("Safari")) {
            deviceInfo.append("Chrome");
        } else if (userAgent.contains("Firefox")) {
            deviceInfo.append("Firefox");
        } else if (userAgent.contains("Safari") && !userAgent.contains("Chrome")) {
            deviceInfo.append("Safari");
        } else if (userAgent.contains("Edge") || userAgent.contains("Edg")) {
            deviceInfo.append("Edge");
        } else if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            deviceInfo.append("Internet Explorer");
        } else {
            deviceInfo.append("Unknown Browser");
        }

        deviceInfo.append(" on ");

        // Xác định hệ điều hành
        if (userAgent.contains("Windows")) {
            deviceInfo.append("Windows");
        } else if (userAgent.contains("Mac OS X")) {
            deviceInfo.append("macOS");
        } else if (userAgent.contains("Android")) {
            deviceInfo.append("Android");
        } else if (userAgent.contains("iPhone") || userAgent.contains("iPad")) {
            deviceInfo.append("iOS");
        } else if (userAgent.contains("Linux")) {
            deviceInfo.append("Linux");
        } else {
            deviceInfo.append("Unknown OS");
        }

        // Xác định loại thiết bị
        if (userAgent.contains("Tablet") || userAgent.contains("iPad")) {
            if (userAgent.contains("iPad")) {
                deviceInfo.append(" (iPad)");
            } else {
                deviceInfo.append(" (Tablet)");
            }
        } else if (userAgent.contains("Mobile")) {
            if (userAgent.contains("iPhone")) {
                deviceInfo.append(" (iPhone)");
            } else {
                deviceInfo.append(" (Mobile)");
            }
        }

        return deviceInfo.toString();
    }
}
