package com.evotek.elasticsearch.domain.repository;

import com.evotek.elasticsearch.domain.User;
import com.evotek.elasticsearch.infrastructure.domainrepository.DocumentDomainRepository;
import com.evotek.elasticsearch.infrastructure.persistence.document.UserDocument;

import java.util.UUID;

public interface UserDomainRepository extends DocumentDomainRepository<User, UUID> {
    void deleteById(UUID userId);
}
