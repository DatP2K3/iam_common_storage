package com.evotek.iam.infrastructure.domainrepository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.evo.common.repository.AbstractDomainRepository;
import com.evotek.iam.domain.OauthClient;
import com.evotek.iam.domain.repository.OauthClientDomainRepository;
import com.evotek.iam.infrastructure.persistence.entity.OauthClientEntity;
import com.evotek.iam.infrastructure.persistence.mapper.OauthClientEntityMapper;
import com.evotek.iam.infrastructure.persistence.repository.OauthClientEntityRepository;
import com.evotek.iam.infrastructure.support.exception.AuthErrorCode;
import com.evotek.iam.infrastructure.support.exception.AuthException;

@Repository
public class OauthClientDomainRepositoryImpl extends AbstractDomainRepository<OauthClient, OauthClientEntity, UUID>
        implements OauthClientDomainRepository {
    private final OauthClientEntityMapper oauthClientEntityMapper;
    private final OauthClientEntityRepository oauthClientEntityRepository;

    public OauthClientDomainRepositoryImpl(
            OauthClientEntityRepository oauthClientEntityRepository, OauthClientEntityMapper oauthClientEntityMapper) {
        super(oauthClientEntityRepository, oauthClientEntityMapper);
        this.oauthClientEntityRepository = oauthClientEntityRepository;
        this.oauthClientEntityMapper = oauthClientEntityMapper;
    }

    @Override
    public OauthClient findByClientId(String clientId) {
        OauthClientEntity oauthClientEntity = oauthClientEntityRepository
                .findByClientId(clientId)
                .orElseThrow(() -> new AuthException(AuthErrorCode.INVALID_CLIENT_SECRET));
        return oauthClientEntityMapper.toDomainModel(oauthClientEntity);
    }

    @Override
    public OauthClient getById(UUID uuid) {
        return oauthClientEntityMapper.toDomainModel(oauthClientEntityRepository
                .findById(uuid)
                .orElseThrow(() -> new AuthException(AuthErrorCode.UNAUTHENTICATED)));
    }
}
