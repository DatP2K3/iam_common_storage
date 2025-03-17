package com.evotek.notification.domain.command;

import java.time.Instant;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StoreNotificationDeliveryCmd {
    private UUID notificationId;
    private UUID deviceRegistrationId;
    private String status;
    private Instant sendAt;
    private Instant seenAt;
}
