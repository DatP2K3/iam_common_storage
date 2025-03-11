package com.evotek.iam.domain.repository;

import java.util.List;
import java.util.UUID;

import com.evotek.iam.domain.Permission;
import com.evotek.iam.domain.query.SearchPermissionQuery;
import com.evotek.iam.infrastructure.domainrepository.DomainRepository;

public interface PermissionDomainRepository extends DomainRepository<Permission, UUID> {
    List<Permission> findPermissionByRoleId(UUID roleId);

    List<Permission> search(SearchPermissionQuery searchPermissionQuery);

    Long count(SearchPermissionQuery searchPermissionQuery);
}
