package com.evotek.iam.domain.repository;

import com.evotek.iam.domain.OauthClient;
import com.evotek.iam.infrastructure.domainrepository.DomainRepository;

import java.util.UUID;

public interface OauthClientDomainRepository extends DomainRepository<OauthClient, UUID> {
    OauthClient findByClientId(String clientId);
}
