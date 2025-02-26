package com.evotek.iam.application.service;

import com.evo.common.dto.response.ApiResponses;
import com.evo.common.dto.response.FileResponse;
import com.evotek.iam.application.dto.request.SearchFileRequest;
import com.evotek.iam.application.dto.request.UpdateFileRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface FileService {
    List<FileResponse> uploadFile(List<MultipartFile> files, boolean isPublic, String description);
    FileResponse getFile(UUID fileId);
    FileResponse updateFile(UpdateFileRequest updateFileRequest);
    void deleteFile(UUID fileId);
    List<FileResponse> search(SearchFileRequest searchFileRequest);
    ApiResponses<Void> testRetry();
}
