package com.evotek.iam.infrastructure.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.evotek.iam.infrastructure.persistence.entity.OauthClientEntity;

public interface OauthClientEntityRepository extends JpaRepository<OauthClientEntity, UUID> {
    Optional<OauthClientEntity> findByClientId(String clientId);
}
