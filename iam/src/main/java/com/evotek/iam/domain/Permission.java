package com.evotek.iam.domain;

import java.util.UUID;

import com.evo.common.Auditor;
import com.evotek.iam.domain.command.CreateOrUpdatePermissionCmd;
import com.evotek.iam.infrastructure.support.IdUtils;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class Permission extends Auditor {
    private UUID id;
    private String resourceId;
    private String scope;
    private boolean deleted;

    public Permission(CreateOrUpdatePermissionCmd cmd) {
        this.id = IdUtils.nextId();
        this.resourceId = cmd.getResourceId();
        this.scope = cmd.getScope();
        this.deleted = false;
    }

    public Permission update(CreateOrUpdatePermissionCmd cmd) {
        this.resourceId = cmd.getResourceId();
        this.scope = cmd.getScope();
        return this;
    }
}
