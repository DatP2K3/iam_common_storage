package com.evotek.iam.domain.repository;

import java.util.List;
import java.util.UUID;

import com.evotek.iam.domain.User;
import com.evotek.iam.domain.query.SearchUserQuery;
import com.evotek.iam.infrastructure.domainrepository.DomainRepository;

public interface UserDomainRepository extends DomainRepository<User, UUID> {
    List<User> search(SearchUserQuery query);

    User getByUsername(String username);

    boolean existsByUsername(String username);

    Long count(SearchUserQuery searchUserQuery);
}
