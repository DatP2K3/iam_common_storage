package com.evotek.iam.infrastructure.adapter.storage;

import com.evo.common.dto.response.ApiResponses;
import com.evo.common.dto.response.FileResponse;
import com.evo.common.dto.response.PageApiResponse;
import com.evotek.iam.application.configuration.FeignStorageClientConfiguration;
import com.evotek.iam.application.dto.request.SearchFileRequest;
import com.evotek.iam.application.dto.request.UpdateFileRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "storage-service",
        url = "${storage-service.url}",
        contextId = "common-iam-with-token",
        configuration = FeignStorageClientConfiguration.class,
        fallbackFactory = StorageClientFallback.class
    )
public interface StorageClient {
    @PostMapping(value = "/api/file/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponses<List<FileResponse>> uploadFiles(@RequestPart("files") List<MultipartFile> files, @RequestParam("isPublic") boolean isPublic, @RequestParam("description") String description);

    @PutMapping(value = "/api/file/update",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ApiResponses<FileResponse> updateFile(@RequestBody UpdateFileRequest updateFileRequest);

    @DeleteMapping("/api/file/delete/{fileId}")
    ApiResponses<Void> deleteFile(@PathVariable("fileId") UUID fileId);

    @GetMapping("/api/file/{fileId}")
    ApiResponses<FileResponse> getFile(@PathVariable("fileId") UUID fileId);

    @PostMapping(value = "/api/file",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    PageApiResponse<List<FileResponse>> searchFiles(@RequestBody SearchFileRequest searchFileRequest);
    @GetMapping("/api/file/test-retry")
    ApiResponses<Void> testRetry();
}