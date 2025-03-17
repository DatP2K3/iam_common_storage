package com.evotek.iam.domain;

import java.util.UUID;

import com.evo.common.Auditor;
import com.evotek.iam.infrastructure.support.IdUtils;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class UserRole extends Auditor {
    private UUID id;
    private UUID userId;
    private UUID roleId;

    public UserRole(UUID roleId, UUID userId) {
        this.id = IdUtils.nextId();
        this.userId = userId;
        this.roleId = roleId;
    }
}
