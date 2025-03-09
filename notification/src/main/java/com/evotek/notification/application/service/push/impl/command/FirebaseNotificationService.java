package com.evotek.notification.application.service.push;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.evo.common.dto.request.PushNotificationRequest;
import com.evotek.notification.infrastructure.support.exception.AppErrorCode;
import com.evotek.notification.infrastructure.support.exception.AppException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FirebaseNotificationService {
    public void sendNotificationToToken(PushNotificationRequest pushNotificationRequest) {
        try {
            Message message = buildNotificationMessage(pushNotificationRequest);
            String response = sendAndGetResponse(message);
        } catch (Exception e) {
            throw new AppException(AppErrorCode.FIREBASE_SEND_NOTIFICATION_FAILED);
        }
    }

    public void sendNotificationToTopic(PushNotificationRequest pushNotificationRequest) {
        try {
            Message message = buildNotificationMessageForTopic(pushNotificationRequest);
            String response = sendAndGetResponse(message);
        } catch (Exception e) {
            throw new AppException(AppErrorCode.FIREBASE_SEND_NOTIFICATION_FAILED);
        }
    }

    private Message buildNotificationMessage(PushNotificationRequest pushNotificationRequest) {
        return Message.builder()
                .setToken(pushNotificationRequest.getToken())
                .setNotification(com.google.firebase.messaging.Notification.builder()
                        .setTitle(pushNotificationRequest.getTitle())
                        .setBody(pushNotificationRequest.getBody())
                        .build())
                .putAllData(pushNotificationRequest.getData() != null ? pushNotificationRequest.getData() : Map.of())
                .build();
    }

    private Message buildNotificationMessageForTopic(PushNotificationRequest pushNotificationRequest) {
        return Message.builder()
                .setTopic(pushNotificationRequest.getTopic())
                .setNotification(com.google.firebase.messaging.Notification.builder()
                        .setTitle(pushNotificationRequest.getTitle())
                        .setBody(pushNotificationRequest.getBody())
                        .build())
                .putAllData(pushNotificationRequest.getData() != null ? pushNotificationRequest.getData() : Map.of())
                .build();
    }

    private String sendAndGetResponse(Message message) throws ExecutionException, InterruptedException {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }
}
