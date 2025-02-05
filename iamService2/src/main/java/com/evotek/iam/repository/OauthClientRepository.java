package com.evotek.iam.repository;

import com.evotek.iam.model.OauthClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OauthClientRepository extends JpaRepository<OauthClient, Integer> {
    Optional<OauthClient> findByClientId(String clientId);
}
