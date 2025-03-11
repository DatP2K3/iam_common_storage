package com.evotek.notification.application.dto.request;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnRegisterDeviceRequest {
    private UUID userId;
    private UUID deviceId;
    private String deviceToken;
}
