package com.evo.common.iam.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import com.evo.common.UserAuthority;
import com.evo.common.dto.response.ApiResponses;
import com.evo.common.enums.ServiceUnavailableError;
import com.evo.common.exception.ForwardInnerAlertException;
import com.evo.common.exception.ResponseException;

import lombok.extern.slf4j.Slf4j;

@Component
public class IamClientFallback
        implements FallbackFactory<IamClient> { // FallbackFactory: Dùng để xử lý khi gặp lỗi khi gọi api từ Iam Client
    @Override
    public IamClient create(Throwable cause) {
        return new FallbackWithFactory(cause);
    }

    @Slf4j
    static class FallbackWithFactory implements IamClient {
        private final Throwable cause;

        FallbackWithFactory(Throwable cause) {
            this.cause = cause;
        }

        @Override
        public ApiResponses<UserAuthority> getUserAuthority(UUID userId) {
            if (cause instanceof ForwardInnerAlertException) {
                return ApiResponses.fail((RuntimeException) cause);
            }
            return ApiResponses.fail(new ResponseException(ServiceUnavailableError.IAM_SERVICE_UNAVAILABLE_ERROR));
        }

        @Override
        public ApiResponses<UserAuthority> getUserAuthority(String username) {
            if (cause instanceof ForwardInnerAlertException) {
                return ApiResponses.fail((RuntimeException) cause);
            }
            return ApiResponses.fail(new ResponseException(ServiceUnavailableError.IAM_SERVICE_UNAVAILABLE_ERROR));
        }
    }
}
