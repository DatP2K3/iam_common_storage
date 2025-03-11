package com.evotek.notification.application.service.push.impl.command;

import org.springframework.stereotype.Service;

import com.evo.common.dto.request.PushNotificationRequest;
import com.evotek.notification.application.mapper.CommandMapper;
import com.evotek.notification.domain.Notification;
import com.evotek.notification.domain.command.StoreNotificationCmd;
import com.evotek.notification.domain.repository.NotificationDomainRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationCommandService {
    private final NotificationDomainRepository notificationDomainRepository;
    private final CommandMapper commandMapper;

    public Notification storeNotification(PushNotificationRequest request) {
        StoreNotificationCmd storeNotificationCmd = commandMapper.from(request);
        Notification notification = new Notification(storeNotificationCmd);
        return notificationDomainRepository.save(notification);
    }
}
