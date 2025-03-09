package com.evotek.iam.infrastructure.persistence.repository.custom;

import java.util.List;

import com.evotek.iam.domain.query.SearchUserQuery;
import com.evotek.iam.infrastructure.persistence.entity.UserEntity;

public interface UserEntityRepositoryCustom {
    List<UserEntity> search(SearchUserQuery searchUserQuery);

    Long count(SearchUserQuery searchUserQuery);
}
