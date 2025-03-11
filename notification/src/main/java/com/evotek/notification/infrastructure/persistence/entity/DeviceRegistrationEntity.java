package com.evotek.notification.infrastructure.persistence.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
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
@Table(name = "device_registrations", uniqueConstraints = @UniqueConstraint(columnNames = {"device_id", "user_id"}))
public class DeviceRegistrationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "device_token")
    private String deviceToken;

    @Column(name = "device_id")
    private UUID deviceId;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "subscribed_topics", columnDefinition = "text[]")
    private List<String> topics = new ArrayList<>();

    @LastModifiedDate
    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    @Column(name = "enabled")
    private boolean enabled;
}
