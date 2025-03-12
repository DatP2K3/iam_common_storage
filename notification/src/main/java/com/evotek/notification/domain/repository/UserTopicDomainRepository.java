package com.evotek.notification.domain.repository;

import java.util.List;
import java.util.UUID;

import com.evotek.notification.domain.UserTopic;
import com.evotek.notification.infrastructure.domainrepository.DomainRepository;

public interface UserTopicDomainRepository extends DomainRepository<UserTopic, UUID> {
    List<String> findTopicEnabled(UUID userId);

    List<UserTopic> findByUserId(UUID userId);

    List<UUID> getUserIdsByTopic(String topic);
}
