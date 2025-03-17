package com.evotek.iam.domain.command;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateOrUpdateRoleCmd {
    private UUID id;
    private String name;
    private String description;
    private boolean root;
    private List<CreateRolePermissionCmd> rolePermission;
}
