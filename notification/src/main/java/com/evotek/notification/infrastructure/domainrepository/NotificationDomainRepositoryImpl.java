package com.evotek.notification.infrastructure.domainrepository;

import com.evotek.notification.domain.Notification;
import com.evotek.notification.domain.repository.NotificationRepository;
import com.evotek.notification.infrastructure.persistence.entity.NotificationEntity;
import com.evotek.notification.infrastructure.persistence.mapper.NotificationEntityMapper;
import com.evotek.notification.infrastructure.persistence.repository.NotificationEntityRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class NotificationRepositoryImpl extends AbstractDomainRepository<Notification, NotificationEntity, UUID>
        implements NotificationRepository {
    private final NotificationEntityMapper notificationEntityMapper;
    private final NotificationEntityRepository notificationEntityRepository;

    public NotificationRepositoryImpl(
            NotificationEntityMapper notificationEntityMapper,
            NotificationEntityRepository notificationEntityRepository){
        super(notificationEntityRepository, notificationEntityMapper);
        this.notificationEntityMapper = notificationEntityMapper;
        this.notificationEntityRepository = notificationEntityRepository;
    }
}
