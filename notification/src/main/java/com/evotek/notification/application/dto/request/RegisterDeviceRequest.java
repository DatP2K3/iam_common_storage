package com.evotek.notification.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDeviceRegistrationRequest {
    private UUID userId;
    private String token;
    private List<String> topics = new ArrayList<>();
    private boolean enabled;
}
