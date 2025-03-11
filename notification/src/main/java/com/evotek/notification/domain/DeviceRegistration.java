package com.evotek.notification.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.evotek.notification.domain.command.RegisterOrUpdateDeviceCmd;
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
public class DeviceRegistration {
    private UUID id;
    private UUID userId;
    private String deviceToken;
    private UUID deviceId;
    private List<String> topics = new ArrayList<>();
    private boolean enabled;
    private List<NotificationDelivery> notificationDeliveries;

    public DeviceRegistration(RegisterOrUpdateDeviceCmd cmd) {
        this.id = IdUtils.nextId();
        this.userId = cmd.getUserId();
        this.deviceToken = cmd.getDeviceToken();
        this.deviceId = cmd.getDeviceId();
        this.enabled = cmd.isEnabled();
    }

    public DeviceRegistration addSubscribedTopic(String topic) {
        if (this.topics == null) {
            this.topics = new ArrayList<>();
        }
        this.topics.add(topic);
        return this;
    }

    public DeviceRegistration removeSubscribedTopic(String topic) {
        if (this.topics == null) {
            return this;
        }
        this.topics.remove(topic);
        return this;
    }

    public void removeAllSubscribedTopics() {
        this.topics = new ArrayList<>();
    }

    public void addNotificationDelivery(StoreNotificationDeliveryCmd cmd) {
        NotificationDelivery notificationDelivery = new NotificationDelivery(cmd);
        if (this.notificationDeliveries == null) {
            this.notificationDeliveries = new ArrayList<>();
        }
        this.notificationDeliveries.add(notificationDelivery);
    }

    public void update(RegisterOrUpdateDeviceCmd registerOrUpdateDeviceCmd) {
        this.topics = registerOrUpdateDeviceCmd.getTopics();
        this.enabled = registerOrUpdateDeviceCmd.isEnabled();
    }
}
