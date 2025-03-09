package com.evotek.iam.presentation.rest;

import java.util.Collections;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evo.common.dto.response.ApiResponses;
import com.evo.common.dto.response.PageApiResponse;
import com.evotek.iam.application.dto.request.CreateOrUpdatePermissionRequest;
import com.evotek.iam.application.dto.request.SearchPermissionRequest;
import com.evotek.iam.application.dto.response.PermissionDTO;
import com.evotek.iam.application.service.PermissionCommandService;
import com.evotek.iam.application.service.PermissionQueryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionCommandService permissionCommandService;
    private final PermissionQueryService permissionQueryService;

    @Operation(
            summary = "Tạo mới quyền",
            description = "API này sẽ tạo mới một quyền trong hệ thống và trả về thông tin.",
            requestBody =
                    @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            description = "Thông tin của quyền cần tạo mới",
                            required = true,
                            content =
                                    @io.swagger.v3.oas.annotations.media.Content(
                                            schema = @Schema(implementation = CreateOrUpdatePermissionRequest.class))),
            responses = {@ApiResponse(responseCode = "201", description = "Quyền đã được tạo mới")})
    @PreAuthorize("hasPermission(null, 'permission.create')")
    @PostMapping("/permissions")
    public ApiResponses<PermissionDTO> createPermission(
            @RequestBody CreateOrUpdatePermissionRequest createOrUpdatePermissionRequest) {
        PermissionDTO permissionDTO = permissionCommandService.createPermission(createOrUpdatePermissionRequest);
        return ApiResponses.<PermissionDTO>builder()
                .data(permissionDTO)
                .success(true)
                .code(201)
                .message("Permission created successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }

    @PreAuthorize("hasPermission(null, 'permission.admin')")
    @PostMapping("/permissions/search")
    PageApiResponse<List<PermissionDTO>> search(@RequestBody SearchPermissionRequest searchPermissionRequest) {
        Long totalPermissions = permissionQueryService.totalPermissions(searchPermissionRequest);
        List<PermissionDTO> permissionDTOs = Collections.emptyList();
        if (totalPermissions != 0) {
            permissionDTOs = permissionQueryService.search(searchPermissionRequest);
        }
        PageApiResponse.PageableResponse pageableResponse = PageApiResponse.PageableResponse.builder()
                .pageSize(searchPermissionRequest.getPageSize())
                .pageIndex(searchPermissionRequest.getPageIndex())
                .totalElements(totalPermissions)
                .totalPages((int) (Math.ceil((double) totalPermissions / searchPermissionRequest.getPageSize())))
                .hasNext(searchPermissionRequest.getPageIndex() + searchPermissionRequest.getPageSize()
                        < totalPermissions)
                .hasPrevious(searchPermissionRequest.getPageIndex() > 1)
                .build();

        return PageApiResponse.<List<PermissionDTO>>builder()
                .data(permissionDTOs)
                .pageable(pageableResponse)
                .success(true)
                .code(200)
                .message("Search permission successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }
}
