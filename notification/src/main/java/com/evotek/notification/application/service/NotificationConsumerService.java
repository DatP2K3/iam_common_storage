package com.evotek.notification.application.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.evo.common.dto.request.PushNotificationRequest;
import com.evo.common.dto.request.SendNotificationRequest;
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
    public void handleSendNotification(SendNotificationRequest request) {
        try {
            if (Channel.EMAIL.name().equals(request.getChannel())) {
                processEmailNotification(request);
            } else if (Channel.SMS.name().equals(request.getChannel())) {
                // Gửi tin nhắn SMS
            } else if (Channel.PUSH_NOTIFICATION.name().equals(request.getChannel())) {
                // Gửi thông báo push
            }
        } catch (Exception e) {
            log.error("Lỗi xử lý thông báo: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "push-notification-group")
    public void handlePushNotification(PushNotificationRequest request) {
        try {
            if (request.getTopic() != null) {
                firebaseNotificationService.sendNotificationToTopic(request);
                Notification notification = notificationCommandService.storeNotification(request);
                List<UUID> userIds = userTopicDomainRepository.getUserIdsByTopic(request.getTopic());
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
                Notification notification = notificationCommandService.storeNotification(request);
                pushNotificationToUser(request, notification.getId());
            }
        } catch (Exception e) {
            log.error("Lỗi xử lý thông báo: {}", e.getMessage(), e);
        }
    }

    private void processEmailNotification(SendNotificationRequest event) {
        // Map templateCode từ event sang template name của Thymeleaf
        String templateName = templateCodeMapper.mapToTemplateName(event.getTemplateCode());

        // Lấy subject dựa vào templateCode
        String subject = templateCodeMapper.getSubject(event.getTemplateCode());

        // Gửi email với template Thymeleaf
        emailService.sendTemplateEmail(event.getRecipient(), subject, templateName, event.getParam());
    }

    private void pushNotificationToUser(PushNotificationRequest request, UUID notificationId) {
        List<DeviceRegistration> deviceRegistrations =
                deviceRegistrationQueryService.getDeviceByUserIdAndEnable(request.getUserId());
        deviceRegistrations.forEach(deviceRegistration -> {
            request.setToken(deviceRegistration.getDeviceToken());
            firebaseNotificationService.sendNotificationToToken(request);
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
