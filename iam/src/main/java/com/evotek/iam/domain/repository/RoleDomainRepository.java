package com.evotek.iam.domain.repository;

import java.util.List;
import java.util.UUID;

import com.evo.common.repository.DomainRepository;
import com.evotek.iam.domain.Permission;
import com.evotek.iam.domain.Role;

public interface RoleDomainRepository extends DomainRepository<Role, UUID> {
    Role findByName(String name);

    List<Permission> findPermissionByRoleId(UUID roleId);
}
