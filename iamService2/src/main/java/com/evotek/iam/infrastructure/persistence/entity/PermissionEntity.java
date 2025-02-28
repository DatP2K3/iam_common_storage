package com.evotek.iam.infrastructure.persistence.entity;

import com.evo.common.AuditEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;


@Entity
@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "permissions")
public class PermissionEntity extends AuditEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "resource_id")
    private String resourceId;

    @Column(name = "scope")
    private String scope;

    @Column(name = "deleted")
    private boolean deleted;
}
