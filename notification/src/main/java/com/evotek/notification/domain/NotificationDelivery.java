package com.evotek.notification.domain;

import java.time.Instant;
import java.util.UUID;

import com.evotek.notification.domain.command.StoreNotificationDeliveryCmd;
import com.evotek.notification.infrastructure.support.IdUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDelivery {
    private UUID id;
    private UUID notificationId;
    private UUID deviceRegistrationId;
    private String status;
    private Instant sendAt;
    private Instant seenAt;

    public NotificationDelivery(StoreNotificationDeliveryCmd cmd) {
        this.id = IdUtils.nextId();
        this.notificationId = cmd.getNotificationId();
        this.deviceRegistrationId = cmd.getDeviceRegistrationId();
        this.status = cmd.getStatus();
        this.sendAt = cmd.getSendAt();
        this.seenAt = cmd.getSeenAt();
    }
}
