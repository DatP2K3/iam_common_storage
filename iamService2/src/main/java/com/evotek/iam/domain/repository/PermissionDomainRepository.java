package com.evotek.iam.domain.repository;

import com.evotek.iam.domain.Permission;
import com.evotek.iam.domain.query.SearchPermissionQuery;
import com.evotek.iam.infrastructure.domainrepository.DomainRepository;

import java.util.List;
import java.util.UUID;

public interface PermissionDomainRepository  extends DomainRepository<Permission, UUID> {
    List<Permission> findPermissionByRoleId(UUID roleId);
    List<Permission> search(SearchPermissionQuery searchPermissionQuery);
    Long count(SearchPermissionQuery searchPermissionQuery);
}
