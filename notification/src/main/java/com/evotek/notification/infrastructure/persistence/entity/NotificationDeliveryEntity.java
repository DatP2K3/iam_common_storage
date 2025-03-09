package com.evotek.notification.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notification_deliveries")
public class NotificationDelivery {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "notification_id")
    private UUID notificationId;

    @Column(name = "device_registration_id")
    private UUID deviceRegistrationId;

    @Column(name = "status")
    private String status;

    @Column(name = "send_at")
    private Long sendAt;

    @Column(name = "seen_at")
    private Long seenAt;
}
