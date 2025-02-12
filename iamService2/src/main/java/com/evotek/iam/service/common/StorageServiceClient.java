package com.evotek.iam.service.common;

import com.evo.common.dto.response.ApiResponses;
import com.evo.common.dto.response.FileResponse;
import com.evo.common.dto.response.PageApiResponse;
import com.evotek.iam.dto.request.FileSearchRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "storage-service",
        url = "${storage-service.url}",
        contextId = "common-iam-with-token",
        fallbackFactory = StorageServiceClientFallback.class
    )
public interface StorageServiceClient {
    @PostMapping(value = "/api/file/uploadl",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponses<List<FileResponse>> uploadFiles(@RequestPart("files") List<MultipartFile> files, @RequestParam("isPublic") boolean isPublic, @RequestParam("description") String description);

    @PutMapping(value = "/api/file/update",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ApiResponses<FileResponse> updateFile(@RequestParam("fileId") int fileId, @RequestParam("file") MultipartFile file);

    @DeleteMapping("/api/file/delete/{fileId}")
    ApiResponses<Void> deleteFile(@PathVariable("fileId") int fileId);

    @GetMapping("/api/file/{fileId}")
    ApiResponses<FileResponse> getFile(@PathVariable("fileId") int fileId);

    @PostMapping(value = "/api/file",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    PageApiResponse<List<FileResponse>> searchFiles(@RequestBody FileSearchRequest fileSearchRequest);
    @CircuitBreaker(name = "default")
    @Retry(name = "default")
    @GetMapping("/api/file/test-retry")
    ApiResponses<Void> testRetry();
}