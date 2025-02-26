package com.evo.common.client.iam;

import com.evo.common.UserAuthority;
import com.evo.common.dto.response.ApiResponses;
import com.evo.common.enums.ServiceUnavailableError;
import com.evo.common.exception.ForwardInnerAlertException;
import com.evo.common.exception.ResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class IamNoTokenClientFallback implements FallbackFactory<IamNoTokenClient> { // FallbackFactory: Dùng để xử lý khi gặp lỗi khi gọi api từ Iam No Token Client
    @Override
    public IamNoTokenClient create(Throwable cause) {
        return new FallbackWithFactory(cause);
    }

    @Slf4j
    static class FallbackWithFactory implements IamNoTokenClient {
        private final Throwable cause;

        FallbackWithFactory(Throwable cause) {
            this.cause = cause;
        }

        @Override
        public ApiResponses<UserAuthority> getClientAuthority(String clientId) {
            if (cause instanceof ForwardInnerAlertException) {
                return ApiResponses.fail((RuntimeException) cause);
            }
            return ApiResponses.fail(
                    new ResponseException(ServiceUnavailableError.IAM_SERVICE_UNAVAILABLE_ERROR));
        }
    }
}
