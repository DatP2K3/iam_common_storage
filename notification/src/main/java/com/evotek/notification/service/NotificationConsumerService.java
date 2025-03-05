package com.evotek.notification.service;

import com.evo.common.mapper.TemplateCodeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailConsumerService {
    private final EmailService emailService;
    private final TemplateCodeMapper templateCodeMapper;

    @KafkaListener(topics = "notification-topic")
    public void handleNotification(NotificationEvent event) {
        try {
            if ("EMAIL".equals(event.getChannel())) {
                processEmailNotification(event);
            }
        } catch (Exception e) {
            log.error("Lỗi xử lý thông báo: {}", e.getMessage(), e);
        }
    }

    private void processEmailNotification(NotificationEvent event) {
        // Map templateCode từ event sang template name của Thymeleaf
        String templateName = templateCodeMapper.mapToTemplateName(event.getTemplateCode());

        // Lấy subject dựa vào templateCode
        String subject = templateCodeMapper.getSubject(event.getTemplateCode());

        // Gửi email với template Thymeleaf
        emailService.sendTemplateEmail(
                event.getRecipient(),
                subject,
                templateName,
                event.getParam()
        );
    }
}
