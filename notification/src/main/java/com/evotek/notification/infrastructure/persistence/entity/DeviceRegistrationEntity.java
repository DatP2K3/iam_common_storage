package com.evotek.notification.infrastructure.persistence.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "device_registrations")
public class DeviceRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "device_token")
    private String token;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "subscribed_topics", columnDefinition = "text[]")
    private List<String> topics = new ArrayList<>();

    @Column(name = "enabled")
    private boolean enabled;
}
