package com.evotek.notification.application.service.push.impl.command;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.evo.common.dto.request.PushNotificationRequest;
import com.evotek.notification.infrastructure.support.exception.AppErrorCode;
import com.evotek.notification.infrastructure.support.exception.AppException;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class FirebaseNotificationService {
    public String sendNotificationToToken(PushNotificationRequest pushNotificationRequest) {
        try {
            Message message = buildNotificationMessage(pushNotificationRequest);
            String response = sendAndGetResponse(message);
            return response;
        } catch (ExecutionException e) {
            handleFirebaseException(e, pushNotificationRequest.getToken());
            throw new AppException(AppErrorCode.FIREBASE_SEND_NOTIFICATION_FAILED);
        } catch (Exception e) {
            throw new AppException(AppErrorCode.FIREBASE_SEND_NOTIFICATION_FAILED);
        }
    }

    public String sendNotificationToTopic(PushNotificationRequest pushNotificationRequest) {
        try {
            Message message = buildNotificationMessageForTopic(pushNotificationRequest);
            String response = sendAndGetResponse(message);
            return response;
        } catch (ExecutionException e) {
            throw new AppException(AppErrorCode.FIREBASE_SEND_NOTIFICATION_FAILED);
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

    @Retryable(
            value = {FirebaseMessagingException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000))
    private String sendAndGetResponse(Message message) throws ExecutionException, InterruptedException {
        return FirebaseMessaging.getInstance(FirebaseApp.getInstance("my-app"))
                .sendAsync(message)
                .get();
    }

    private void handleFirebaseException(ExecutionException e, String token) {
        if (e.getCause() instanceof FirebaseMessagingException) {
            FirebaseMessagingException firebaseEx = (FirebaseMessagingException) e.getCause();
            log.error(
                    "Firebase messaging error: {}, code: {}",
                    firebaseEx.getMessage(),
                    firebaseEx.getMessagingErrorCode());

            // Handle invalid token
            if (firebaseEx.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED) {
                log.warn("Token unregistered, should be removed: {}", token);
                // TODO: You can add a call to your repository to remove or invalidate the token
                // deviceRepository.deleteByToken(token);
            }
        }
    }
}
