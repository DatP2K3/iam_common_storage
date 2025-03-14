package com.evotek.iam.domain.repository;

import java.util.UUID;

import com.evo.common.repository.DomainRepository;
import com.evotek.iam.domain.OauthClient;

public interface OauthClientDomainRepository extends DomainRepository<OauthClient, UUID> {
    OauthClient findByClientId(String clientId);
}
