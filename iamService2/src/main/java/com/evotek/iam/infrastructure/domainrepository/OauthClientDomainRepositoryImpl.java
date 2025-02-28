package com.evotek.iam.infrastructure.domainrepository;

import com.evotek.iam.domain.OauthClient;
import com.evotek.iam.domain.repository.OauthClientDomainRepository;
import com.evotek.iam.infrastructure.persistence.entity.OauthClientEntity;
import com.evotek.iam.infrastructure.persistence.mapper.OauthClientEntityMapper;
import com.evotek.iam.infrastructure.persistence.repository.OauthClientEntityRepository;
import com.evotek.iam.infrastructure.support.exception.AuthErrorCode;
import com.evotek.iam.infrastructure.support.exception.AuthException;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class OauthClientDomainRepositoryImpl extends AbstractDomainRepository<OauthClient, OauthClientEntity, UUID>
        implements OauthClientDomainRepository {
    private final OauthClientEntityMapper entityMapper;
    private final OauthClientEntityRepository repository;

    public OauthClientDomainRepositoryImpl(OauthClientEntityRepository repository, OauthClientEntityMapper entityMapper) {
        super(repository, entityMapper);
        this.repository = repository;
        this.entityMapper = entityMapper;}

    @Override
    public OauthClient findByClientId(String clientId) {
        OauthClientEntity oauthClientEntity = repository.findByClientId(clientId).orElseThrow(() -> new AuthException(AuthErrorCode.INVALID_CLIENT_SECRET));
        return entityMapper.toDomainModel(oauthClientEntity);
    }

    @Override
    public OauthClient getById(UUID uuid) {
        return entityMapper.toDomainModel(repository.findById(uuid).orElseThrow(() -> new AuthException(AuthErrorCode.UNAUTHENTICATED)));
    }
}
