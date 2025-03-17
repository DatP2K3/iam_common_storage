package com.evotek.notification.infrastructure.persistence.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EnableJpaAuditing
@Table(name = "notification_deliveries")
public class NotificationDeliveryEntity {
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

    @CreatedDate
    @Column(name = "send_at")
    private Instant sendAt;

    @LastModifiedDate
    @Column(name = "seen_at")
    private Instant seenAt;
}
