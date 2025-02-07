package com.evotek.iam.service.common;

import com.evo.common.dto.response.ApiResponses;
import com.evo.common.dto.response.FileResponse;
import com.evotek.iam.dto.request.FileSearchRequest;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService{
    private final StorageServiceClient storageServiceClient;
    @Override
    public List<FileResponse> uploadFile(List<MultipartFile> files, boolean isPublic, String description) {
        return storageServiceClient.uploadFiles(files, isPublic, description).getData();

    }

    @Override
    public FileResponse getFile(int fileId) {
        return storageServiceClient.getFile(fileId).getData();
    }

    @Override
    public FileResponse updateFile(int fieldId, MultipartFile file) {
        return storageServiceClient.updateFile(fieldId, file).getData();
    }

    @Override
    public void deleteFile(int fileId) {
        storageServiceClient.deleteFile(fileId);
    }

    @Override
    public List<FileResponse> search(FileSearchRequest fileSearchRequest){
        return storageServiceClient.searchFiles(fileSearchRequest).getData();
    }

    @Override
    public ApiResponses<Void> testRetry() {
        return storageServiceClient.testRetry();
    }
}
