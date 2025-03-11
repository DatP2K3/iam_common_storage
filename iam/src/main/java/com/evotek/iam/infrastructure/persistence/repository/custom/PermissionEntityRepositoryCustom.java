package com.evotek.iam.infrastructure.persistence.repository.custom;

import java.util.List;

import com.evotek.iam.domain.query.SearchPermissionQuery;
import com.evotek.iam.infrastructure.persistence.entity.PermissionEntity;

public interface PermissionEntityRepositoryCustom {
    List<PermissionEntity> search(SearchPermissionQuery searchPermissionQuery);

    Long count(SearchPermissionQuery searchPermissionQuery);
}
