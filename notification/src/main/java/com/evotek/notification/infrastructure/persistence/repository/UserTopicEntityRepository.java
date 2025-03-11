package com.evotek.notification.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import com.evotek.notification.domain.UserTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.evotek.notification.infrastructure.persistence.entity.UserTopicEntity;

public interface UserTopicEntityRepository extends JpaRepository<UserTopicEntity, UUID> {

    @Query("SELECT t.topic FROM UserTopicEntity t WHERE :userId = t.userId AND t.enabled = true")
    List<String> findTopicEnabled(@Param("userId") UUID userId);

    List<UserTopicEntity> findByUserIdAndEnabledTrue(UUID userId);
    List<UserTopicEntity> findByUserId(UUID userId);
}
