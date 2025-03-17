package com.evotek.notification.infrastructure.domainrepository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.evo.common.repository.AbstractDomainRepository;
import com.evotek.notification.domain.Notification;
import com.evotek.notification.domain.repository.NotificationDomainRepository;
import com.evotek.notification.infrastructure.persistence.entity.NotificationEntity;
import com.evotek.notification.infrastructure.persistence.mapper.NotificationEntityMapper;
import com.evotek.notification.infrastructure.persistence.repository.NotificationEntityRepository;
import com.evotek.notification.infrastructure.support.exception.AppErrorCode;
import com.evotek.notification.infrastructure.support.exception.AppException;

@Repository
public class NotificationDomainRepositoryImpl extends AbstractDomainRepository<Notification, NotificationEntity, UUID>
        implements NotificationDomainRepository {
    private final NotificationEntityMapper notificationEntityMapper;
    private final NotificationEntityRepository notificationEntityRepository;

    public NotificationDomainRepositoryImpl(
            NotificationEntityMapper notificationEntityMapper,
            NotificationEntityRepository notificationEntityRepository) {
        super(notificationEntityRepository, notificationEntityMapper);
        this.notificationEntityMapper = notificationEntityMapper;
        this.notificationEntityRepository = notificationEntityRepository;
    }

    @Override
    public Notification getById(UUID uuid) {
        NotificationEntity notificationEntity = notificationEntityRepository
                .findById(uuid)
                .orElseThrow(() -> new AppException(AppErrorCode.NOTIFICATION_NOT_FOUND));
        return notificationEntityMapper.toDomainModel(notificationEntity);
    }
}
