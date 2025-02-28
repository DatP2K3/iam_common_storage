package com.evotek.iam.infrastructure.persistence.repository.custom;

import com.evotek.iam.domain.query.SearchPermissionQuery;
import com.evotek.iam.domain.query.SearchUserQuery;
import com.evotek.iam.infrastructure.persistence.entity.PermissionEntity;
import com.evotek.iam.infrastructure.persistence.entity.UserEntity;

import java.util.List;

public interface PermissionEntityRepositoryCustom {
    List<PermissionEntity> search(SearchPermissionQuery searchPermissionQuery);
    Long count(SearchPermissionQuery searchPermissionQuery);
}
