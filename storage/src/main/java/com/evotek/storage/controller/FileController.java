package com.evotek.storage.controller;

import com.evo.common.dto.response.ApiResponses;
import com.evo.common.dto.response.FileResponse;
import com.evo.common.dto.response.PageApiResponse;
import com.evotek.storage.dto.request.FileSearchRequest;
import com.evotek.storage.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PreAuthorize("hasPermission(null, 'file.create')")
    @PostMapping("/upload")
    public ApiResponses<List<FileResponse>> storeFile(@RequestPart List<MultipartFile> files, boolean isPublic, String description) {
        List<FileResponse> fileResponses = fileService.storeFile(files, isPublic, description);
        return ApiResponses.<List<FileResponse>>builder()
                .data(fileResponses)
                .success(true)
                .code(201)
                .message("Files stored successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }

    @PreAuthorize("hasPermission(null, 'file.read')")
    @GetMapping("/{filedId}")
    public ApiResponses<FileResponse> getFile(@PathVariable int filedId) {
        FileResponse fileResponse = fileService.getPrivateFile(filedId);
        return ApiResponses.<FileResponse>builder()
                .data(fileResponse)
                .success(true)
                .code(200)
                .message("File retrieved successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }

    @PreAuthorize("hasPermission(null, 'file.update')")
    @PutMapping("/update")
    public ApiResponses<FileResponse> updateFile(@RequestParam int fileId, @RequestParam String fileName, @RequestParam String description) {
        FileResponse fileResponse = fileService.updateFile(fileId, fileName, description);
        return ApiResponses.<FileResponse>builder()
                .data(fileResponse)
                .success(true)
                .code(200)
                .message("File updated successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }

    @PreAuthorize("hasPermission(null, 'file.delete')")
    @DeleteMapping("/delete/{fileId}")
    public ApiResponses<Void> deleteFile(@PathVariable int fileId) {
        fileService.deleteFile(fileId);
        return ApiResponses.<Void>builder()
                .success(true)
                .code(204)
                .message("File deleted successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }

    @PreAuthorize("hasPermission(null, 'file.read')")
    @GetMapping()
    public PageApiResponse<List<FileResponse>> search(@RequestBody FileSearchRequest fileSearchRequest) {
        List<FileResponse> fileResponses = fileService.search(fileSearchRequest);
        PageApiResponse.PageableResponse pageableResponse = PageApiResponse.PageableResponse.builder()
                .pageSize(fileSearchRequest.getPageSize())
                .pageIndex(fileSearchRequest.getPageIndex()).build();
        return PageApiResponse.<List<FileResponse>>builder()
                .data(fileResponses)
                .pageable(pageableResponse)
                .success(true)
                .code(200)
                .message("Get files successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }

    @GetMapping("/test-retry")
    public ApiResponses<Void> testRetry() throws InterruptedException, IOException {
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        throw new IOException("Test retry failed");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        Thread.sleep(18000);
        return ApiResponses.<Void>builder()
                .success(true)
                .code(200)
                .message("Test retry successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }
}
