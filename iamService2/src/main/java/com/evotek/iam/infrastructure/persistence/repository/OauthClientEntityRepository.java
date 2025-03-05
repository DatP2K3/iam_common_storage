package com.evotek.iam.infrastructure.persistence.repository;

import com.evotek.iam.infrastructure.persistence.entity.OauthClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OauthClientEntityRepository extends JpaRepository<OauthClientEntity, UUID> {
    Optional<OauthClientEntity> findByClientId(String clientId);
}
