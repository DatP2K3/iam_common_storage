package com.evo.common.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PushNotificationRequest {
    private String title;
    private String body;
    private String imageUrl;
    private String topic;
    private String token;
    private Map<String, String> data;
}
