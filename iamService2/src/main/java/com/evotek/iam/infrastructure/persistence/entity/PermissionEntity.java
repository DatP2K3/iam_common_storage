package com.evotek.iam.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "permissions")
public class PermissionEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "resource_id")
    private String resourceId;

    @Column(name = "scope")
    private String scope;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public static PermissionEntity creatPermission(String resourceId, String scope) {
            PermissionEntity permissionEntity = PermissionEntity.builder()
                    .resourceId(resourceId)
                    .scope(scope)
                    .build();
        return permissionEntity;
    }
}
