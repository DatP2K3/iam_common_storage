package com.evotek.notification.application.dto.request;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterOrUpdateDeviceRequest {
    private UUID userId;
    private String deviceToken;
    private UUID deviceId;
    private List<String> topics = new ArrayList<>();
    private boolean enabled;
}
