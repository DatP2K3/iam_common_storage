package com.evotek.notification.application.service.send;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.evo.common.dto.request.SendNotificationRequest;
import com.evo.common.enums.Channel;
import com.evo.common.mapper.TemplateCodeMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumerService {
    private final EmailService emailService;
    private final TemplateCodeMapper templateCodeMapper;

    @KafkaListener(topics = "notification-group")
    public void handleNotification(SendNotificationRequest event) {
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

    private void processEmailNotification(SendNotificationRequest event) {
        // Map templateCode từ event sang template name của Thymeleaf
        String templateName = templateCodeMapper.mapToTemplateName(event.getTemplateCode());

        // Lấy subject dựa vào templateCode
        String subject = templateCodeMapper.getSubject(event.getTemplateCode());

        // Gửi email với template Thymeleaf
        emailService.sendTemplateEmail(event.getRecipient(), subject, templateName, event.getParam());
    }
}
