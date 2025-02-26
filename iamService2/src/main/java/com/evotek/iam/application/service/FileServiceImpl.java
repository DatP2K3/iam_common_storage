package com.evotek.iam.application.service;

import com.evo.common.dto.response.ApiResponses;
import com.evo.common.dto.response.FileResponse;
import com.evotek.iam.application.dto.request.SearchFileRequest;
import com.evotek.iam.application.dto.request.UpdateFileRequest;
import com.evotek.iam.infrastructure.adapter.storage.StorageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final StorageClient storageClient;
    @Override
    public List<FileResponse> uploadFile(List<MultipartFile> files, boolean isPublic, String description) {
        return storageClient.uploadFiles(files, isPublic, description).getData();
    }

    @Override
    public FileResponse getFile(UUID fileId) {
        return storageClient.getFile(fileId).getData();
    }

    @Override
    public FileResponse updateFile(UpdateFileRequest updateFileRequest) {
        return storageClient.updateFile(updateFileRequest).getData();
    }

    @Override
    public void deleteFile(UUID fileId) {
        storageClient.deleteFile(fileId);
    }

    @Override
    public List<FileResponse> search(SearchFileRequest searchFileRequest){
        return storageClient.searchFiles(searchFileRequest).getData();
    }

    @Override
    public ApiResponses<Void> testRetry() {
        return storageClient.testRetry();
    }
}
