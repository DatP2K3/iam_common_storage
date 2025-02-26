package com.evotek.iam.domain.repository;

import com.evotek.iam.domain.User;
import com.evotek.iam.domain.UserRole;
import com.evotek.iam.domain.query.SearchUserQuery;
import com.evotek.iam.infrastructure.domainrepository.DomainRepository;

import java.util.List;
import java.util.UUID;

public interface UserDomainRepository extends DomainRepository<User, UUID> {
    List<User> search(SearchUserQuery query);
    User findByUsername(String username);
    boolean existsByUsername(String username);
    Long count(SearchUserQuery searchUserQuery);
}
