package com.evotek.iam.domain.repository;

import com.evotek.iam.domain.Permission;
import com.evotek.iam.domain.Role;
import com.evotek.iam.infrastructure.domainrepository.DomainRepository;

import java.util.List;
import java.util.UUID;

public interface RoleDomainRepository extends DomainRepository<Role, UUID> {
    Role findByName(String name);
    List<Permission> findPermissionByRoleId(UUID roleId);
}
