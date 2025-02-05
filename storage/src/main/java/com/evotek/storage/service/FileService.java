package com.evotek.storage.service;

import com.evo.common.dto.response.FileResponse;
import com.evotek.storage.dto.request.FileSearchRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    List<FileResponse> storeFile(List<MultipartFile> files, boolean isPublic, String description);
    FileResponse updateFile(int fieldId, String fileName, String description);
    void deleteFile(int fileId);
    List<FileResponse> search(FileSearchRequest fileSearchRequest);
    FileResponse getPrivateFile(int filedId);
    FileResponse getPublicFile(int filedId);
}
