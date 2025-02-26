package com.evotek.iam.domain;

import com.evotek.iam.domain.command.CreateUserRoleCmd;
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
public class UserRole extends Auditor {
    private UUID id;
    private UUID userId;
    private UUID roleId;

    public UserRole(CreateUserRoleCmd createUserRoleCmd, UUID userId) {
        this.id = IdUtils.nextId();
        this.userId = userId;
        this.roleId = createUserRoleCmd.getRoleId();
    }
}
