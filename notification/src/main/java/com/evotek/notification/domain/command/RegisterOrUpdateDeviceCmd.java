package com.evotek.notification.domain.command;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterOrUpdateDeviceCmd {
    private UUID userId;
    private String deviceToken;
    private UUID deviceId;
    private List<String> topics = new ArrayList<>();
    private boolean enabled;
}
