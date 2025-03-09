package com.evo.common.dto.request;

import java.util.Map;

import com.evo.common.enums.TemplateCode;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendNotificationRequest {
    private String channel;
    private String recipient;
    private TemplateCode templateCode;
    private Map<String, Object> param;
}
