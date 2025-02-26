package com.evotek.iam.infrastructure.persistence.repository.custom;

import com.evotek.iam.domain.query.SearchUserQuery;
import com.evotek.iam.infrastructure.persistence.entity.UserEntity;

import java.util.List;

public interface UserEntityRepositoryCustom {
    List<UserEntity> search(SearchUserQuery searchUserQuery);
    Long count(SearchUserQuery searchUserQuery);
}
