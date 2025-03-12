package com.evotek.iam.infrastructure.adapter.notification;

import java.util.UUID;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import com.evo.common.enums.ServiceUnavailableError;
import com.evo.common.exception.ForwardInnerAlertException;
import com.evo.common.exception.ResponseException;

import lombok.extern.slf4j.Slf4j;

@Component
public class NotificationClientFallback implements FallbackFactory<NotificationClient> {
    @Override
    public NotificationClient create(Throwable cause) {
        return new FallbackWithFactory(cause);
    } // FallbackFactory: Dùng để xử lý khi gặp lỗi khi gọi api từ Iam Client

    @Slf4j
    static class FallbackWithFactory implements NotificationClient {
        private final Throwable cause;

        FallbackWithFactory(Throwable cause) {
            this.cause = cause;
        }

        @Override
        public void initUserTopic(UUID userId) {
            if (cause instanceof ForwardInnerAlertException) {
                throw (RuntimeException) cause;
            }
            throw new ResponseException(ServiceUnavailableError.NOTIFICATION_SERVICE_UNAVAILABLE_ERROR);
        }
    }
}
