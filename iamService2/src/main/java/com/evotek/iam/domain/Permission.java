package com.evotek.iam.domain;

import com.evotek.iam.domain.command.CreateOrUpdatePermissionCmd;
import com.evotek.iam.infrastructure.support.IdUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

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

    public Permission(CreateOrUpdatePermissionCmd cmd) {
        this.id = IdUtils.nextId();
        this.resourceId = cmd.getResourceId();
        this.scope = cmd.getScope();
    }

    public Permission update(CreateOrUpdatePermissionCmd cmd) {
        this.resourceId = cmd.getResourceId();
        this.scope = cmd.getScope();
        return this;
    }
}
