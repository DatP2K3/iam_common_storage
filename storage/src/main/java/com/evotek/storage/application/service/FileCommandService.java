package com.evotek.storage.application.service;

import com.evo.common.dto.response.FileResponse;
import com.evotek.storage.application.dto.request.UpdateFileRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface FileCommandService {
    List<FileResponse> storeFile(List<MultipartFile> files, boolean isPublic, String description);
    FileResponse updateFile(UpdateFileRequest updateFileRequest);
    void deleteFile(UUID fileId);
}
