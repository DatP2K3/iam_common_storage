package com.evotek.iam.service.common;

import com.evo.common.UserAuthority;
import com.evo.common.client.iam.IamClient;
import com.evo.common.dto.response.ApiResponses;
import com.evo.common.dto.response.FileResponse;
import com.evo.common.dto.response.PageApiResponse;
import com.evo.common.enums.ServiceUnavailableError;
import com.evo.common.exception.ForwardInnerAlertException;
import com.evo.common.exception.ResponseException;
import com.evotek.iam.dto.request.FileSearchRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class StorageServiceClientFallback implements FallbackFactory<StorageServiceClient> { // FallbackFactory: Dùng để xử lý khi gặp lỗi khi gọi api từ Iam Client
    @Override
    public StorageServiceClient create(Throwable cause) {
        return new FallbackWithFactory(cause);
    }

    @Slf4j
    static class FallbackWithFactory implements StorageServiceClient {
        private final Throwable cause;

        FallbackWithFactory(Throwable cause) {
            this.cause = cause;
        }


        @Override
        public ApiResponses<List<FileResponse>> uploadFiles(List<MultipartFile> files, boolean isPublic, String description) {
            if (cause instanceof ForwardInnerAlertException) {
                return ApiResponses.fail((RuntimeException) cause);
            }
            return ApiResponses.fail(
                    new ResponseException(ServiceUnavailableError.STORAGE_SERVICE_UNAVAILABLE_ERROR));
        }

        @Override
        public ApiResponses<FileResponse> updateFile(int fileId, MultipartFile file) {
            if (cause instanceof ForwardInnerAlertException) {
                return ApiResponses.fail((RuntimeException) cause);
            }
            return ApiResponses.fail(
                    new ResponseException(ServiceUnavailableError.STORAGE_SERVICE_UNAVAILABLE_ERROR));
        }

        @Override
        public ApiResponses<Void> deleteFile(int fileId) {
            if (cause instanceof ForwardInnerAlertException) {
                return ApiResponses.fail((RuntimeException) cause);
            }
            return ApiResponses.fail(
                    new ResponseException(ServiceUnavailableError.STORAGE_SERVICE_UNAVAILABLE_ERROR));
        }

        @Override
        public ApiResponses<FileResponse> getFile(int fileId) {
            if (cause instanceof ForwardInnerAlertException) {
                return ApiResponses.fail((RuntimeException) cause);
            }
            return ApiResponses.fail(
                    new ResponseException(ServiceUnavailableError.STORAGE_SERVICE_UNAVAILABLE_ERROR));
        }

        @Override
        public PageApiResponse<List<FileResponse>> searchFiles(FileSearchRequest fileSearchRequest) {
            if (cause instanceof ForwardInnerAlertException) {
                return PageApiResponse.fail((RuntimeException) cause);
            }
            return PageApiResponse.fail(
                    new ResponseException(ServiceUnavailableError.STORAGE_SERVICE_UNAVAILABLE_ERROR));
        }

        @Override
        public ApiResponses<Void> testRetry() {
            if (cause instanceof ForwardInnerAlertException) {
                return ApiResponses.fail((RuntimeException) cause);
            }
            return ApiResponses.fail(
                    new ResponseException(ServiceUnavailableError.STORAGE_SERVICE_UNAVAILABLE_ERROR));
        }
    }
}
