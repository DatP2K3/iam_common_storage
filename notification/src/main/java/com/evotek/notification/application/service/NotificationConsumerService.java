package com.evotek.notification.application.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.evo.common.dto.event.PushNotificationEvent;
import com.evo.common.dto.event.SendNotificationEvent;
import com.evo.common.enums.Channel;
import com.evo.common.mapper.TemplateCodeMapper;
import com.evotek.notification.application.service.push.impl.command.FirebaseNotificationService;
import com.evotek.notification.application.service.push.impl.command.NotificationCommandService;
import com.evotek.notification.application.service.push.impl.query.DeviceRegistrationQueryService;
import com.evotek.notification.application.service.send.EmailService;
import com.evotek.notification.domain.DeviceRegistration;
import com.evotek.notification.domain.Notification;
import com.evotek.notification.domain.command.StoreNotificationDeliveryCmd;
import com.evotek.notification.domain.repository.DeviceRegistrationDomainRepository;
import com.evotek.notification.domain.repository.UserTopicDomainRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumerService {
    private final EmailService emailService;
    private final TemplateCodeMapper templateCodeMapper;
    private final NotificationCommandService notificationCommandService;
    private final DeviceRegistrationQueryService deviceRegistrationQueryService;
    private final FirebaseNotificationService firebaseNotificationService;
    private final DeviceRegistrationDomainRepository deviceRegistrationDomainRepository;
    private final UserTopicDomainRepository userTopicDomainRepository;

    @KafkaListener(topics = "send-notification-group")
    public void handleSendNotification(SendNotificationEvent event) {
        try {
            if (Channel.EMAIL.name().equals(event.getChannel())) {
                processEmailNotification(event);
            } else if (Channel.SMS.name().equals(event.getChannel())) {
                // Gửi tin nhắn SMS
            } else if (Channel.PUSH_NOTIFICATION.name().equals(event.getChannel())) {
                // Gửi thông báo push
            }
        } catch (Exception e) {
            log.error("Lỗi xử lý thông báo: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "push-notification-group")
    public void handlePushNotification(PushNotificationEvent event) {
        try {
            if (event.getTopic() != null) {
                firebaseNotificationService.sendNotificationToTopic(event);
                Notification notification = notificationCommandService.storeNotification(event);
                List<UUID> userIds = userTopicDomainRepository.getUserIdsByTopic(event.getTopic());
                List<DeviceRegistration> deviceRegistrations =
                        deviceRegistrationQueryService.getDevicesByListUserId(userIds);
                deviceRegistrations.forEach(deviceRegistration -> {
                    StoreNotificationDeliveryCmd storeNotificationDeliveryCmd = StoreNotificationDeliveryCmd.builder()
                            .notificationId(notification.getId())
                            .deviceRegistrationId(deviceRegistration.getId())
                            .status("SENT")
                            .sendAt(Instant.ofEpochSecond(System.currentTimeMillis()))
                            .build();
                    deviceRegistration.addNotificationDelivery(storeNotificationDeliveryCmd);
                    deviceRegistrationDomainRepository.save(deviceRegistration);
                });
            } else {
                Notification notification = notificationCommandService.storeNotification(event);
                pushNotificationToUser(event, notification.getId());
            }
        } catch (Exception e) {
            log.error("Lỗi xử lý thông báo: {}", e.getMessage(), e);
        }
    }

    private void processEmailNotification(SendNotificationEvent event) {
        // Map templateCode từ event sang template name của Thymeleaf
        String templateName = templateCodeMapper.mapToTemplateName(event.getTemplateCode());

        // Lấy subject dựa vào templateCode
        String subject = templateCodeMapper.getSubject(event.getTemplateCode());

        // Gửi email với template Thymeleaf
        emailService.sendTemplateEmail(event.getRecipient(), subject, templateName, event.getParam());
    }

    private void pushNotificationToUser(PushNotificationEvent event, UUID notificationId) {
        List<DeviceRegistration> deviceRegistrations =
                deviceRegistrationQueryService.getDeviceByUserIdAndEnable(event.getUserId());
        deviceRegistrations.forEach(deviceRegistration -> {
            event.setToken(deviceRegistration.getDeviceToken());
            firebaseNotificationService.sendNotificationToToken(event);
            StoreNotificationDeliveryCmd storeNotificationDeliveryCmd = StoreNotificationDeliveryCmd.builder()
                    .notificationId(notificationId)
                    .deviceRegistrationId(deviceRegistration.getId())
                    .status("SENT")
                    .sendAt(Instant.ofEpochSecond(System.currentTimeMillis()))
                    .build();
            deviceRegistration.addNotificationDelivery(storeNotificationDeliveryCmd);
            deviceRegistrationDomainRepository.save(deviceRegistration);
        });
    }
}
