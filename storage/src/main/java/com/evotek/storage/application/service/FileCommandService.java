package com.evotek.storage.application.service;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.evo.common.dto.response.FileResponse;
import com.evotek.storage.application.dto.request.UpdateFileRequest;

public interface FileCommandService {
    List<FileResponse> storeFile(List<MultipartFile> files, boolean isPublic, String description);

    FileResponse updateFile(UpdateFileRequest updateFileRequest);

    void deleteFile(UUID fileId);
}
