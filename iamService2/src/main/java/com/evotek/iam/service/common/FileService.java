package com.evotek.iam.service.common;

import com.evo.common.dto.response.FileResponse;
import com.evotek.iam.dto.request.FileSearchRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    List<FileResponse> uploadFile(List<MultipartFile> files, boolean isPublic, String description);
    FileResponse getFile(int fileId);
    FileResponse updateFile(int fieldId, MultipartFile file);
    void deleteFile(int fileId);
    List<FileResponse> search(FileSearchRequest fileSearchRequest);
}
