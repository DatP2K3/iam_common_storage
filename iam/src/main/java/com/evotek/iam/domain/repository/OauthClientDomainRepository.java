package com.evotek.iam.domain.repository;

import java.util.UUID;

import com.evotek.iam.domain.OauthClient;
import com.evotek.iam.infrastructure.domainrepository.DomainRepository;

public interface OauthClientDomainRepository extends DomainRepository<OauthClient, UUID> {
    OauthClient findByClientId(String clientId);
}
