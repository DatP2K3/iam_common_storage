package com.evotek.iam.domain.repository;

import java.util.List;
import java.util.UUID;

import com.evotek.iam.domain.Permission;
import com.evotek.iam.domain.Role;
import com.evotek.iam.infrastructure.domainrepository.DomainRepository;

public interface RoleDomainRepository extends DomainRepository<Role, UUID> {
    Role findByName(String name);

    List<Permission> findPermissionByRoleId(UUID roleId);
}
