package com.evotek.iam.presentation.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.evo.common.dto.response.ApiResponses;
import com.evotek.iam.application.dto.request.CreateOrUpdateRoleRequest;
import com.evotek.iam.application.dto.response.RoleDTO;
import com.evotek.iam.application.service.RoleCommandService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RoleController {
    private final RoleCommandService roleCommandService;

    @Operation(
            summary = "Tạo mới vai trò",
            description = "API này sẽ tạo mới một vai trò trong hệ thống và trả về thông tin.",
            requestBody =
                    @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            description = "Thông tin của vai trò cần tạo mới",
                            required = true,
                            content =
                                    @io.swagger.v3.oas.annotations.media.Content(
                                            schema = @Schema(implementation = CreateOrUpdateRoleRequest.class))),
            responses = {@ApiResponse(responseCode = "201", description = "Vai trò đã được tạo mới")})
    @PreAuthorize("hasPermission(null, 'role.create')")
    @PostMapping("/roles")
    public ApiResponses<RoleDTO> createRole(@RequestBody CreateOrUpdateRoleRequest createOrUpdateRoleRequest) {
        RoleDTO RoleDTO = roleCommandService.createRole(createOrUpdateRoleRequest);
        return ApiResponses.<RoleDTO>builder()
                .data(RoleDTO)
                .success(true)
                .code(201)
                .message("Role created successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }

    @PreAuthorize("hasPermission(null, 'role.update')")
    @PutMapping("/roles")
    public ApiResponses<RoleDTO> updateRole(@RequestBody CreateOrUpdateRoleRequest createOrUpdateRoleRequest) {
        RoleDTO RoleDTO = roleCommandService.updateRole(createOrUpdateRoleRequest);
        return ApiResponses.<RoleDTO>builder()
                .data(RoleDTO)
                .success(true)
                .code(200)
                .message("Role updated successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }
}
