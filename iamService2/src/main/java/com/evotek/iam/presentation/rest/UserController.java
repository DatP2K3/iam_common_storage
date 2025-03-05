package com.evotek.iam.presentation.rest;

import com.evo.common.UserAuthority;
import com.evo.common.dto.response.ApiResponses;
import com.evo.common.dto.response.PageApiResponse;
import com.evotek.iam.application.dto.request.CreateUserRequest;
import com.evotek.iam.application.dto.request.ChangePasswordRequest;
import com.evotek.iam.application.dto.request.UpdateUserRequest;
import com.evotek.iam.application.dto.request.SearchUserRequest;
import com.evotek.iam.application.dto.response.UserDTO;
import com.evotek.iam.infrastructure.adapter.storage.FileService;
import com.evotek.iam.application.service.UserCommandService;
import com.evotek.iam.application.service.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;
    private final FileService fileService;

    @PreAuthorize("hasPermission(null, 'user.admin')")
    @GetMapping("/users/authorities/{userId}")
    public UserAuthority getUserAuthority(@PathVariable UUID userId) {
        return null;
    }

    @PreAuthorize("hasPermission(null, 'user.admin')")
    @GetMapping("/users/authorities-by-username/{username}")
    public ApiResponses<UserAuthority> getUserAuthority(@PathVariable String username) {
        UserAuthority userAuthority = userQueryService.getUserAuthority(username);
        return ApiResponses.<UserAuthority>builder()
                .data(userAuthority)
                .success(true)
                .code(200)
                .message("Get client authority successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }

    @GetMapping("/client/authorities/{clientId}")
    public ApiResponses<UserAuthority> getClientAuthority(@PathVariable String clientId) {
        UserAuthority userAuthority = userQueryService.getClientAuthority(clientId);
        return ApiResponses.<UserAuthority>builder()
                .data(userAuthority)
                .success(true)
                .code(200)
                .message("Get client authority successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }

    @Operation(summary = "Tạo mới người dùng",
            description = "API này sẽ tạo mới một người dùng trong hệ thống và trả về thông tin.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Thông tin của người dùng cần tạo mới",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = CreateUserRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Người dùng đã được tạo mới")
            })
    @PostMapping("/users")
    public ApiResponses<UserDTO> createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        UserDTO userDTO = userCommandService.createDefaultUser(createUserRequest);
        return ApiResponses.<UserDTO>builder()
                .data(userDTO)
                .success(true)
                .code(201)
                .message("User created successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }

    @Operation(summary = "Tạo mới người quản trị",
            description = "API này sẽ tạo mới một người quản trị trong hệ thống và trả về thông tin.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Thông tin của người quản trị cần tạo mới",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = CreateUserRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Người quản trị đã được tạo mới")
            })
    @PreAuthorize("hasPermission(null, 'user.admin')")
    @PostMapping("/administers")
    public ApiResponses<UserDTO> createAdminister(@RequestBody @Valid CreateUserRequest createUserRequest) {
        UserDTO userDTO = userCommandService.createUser(createUserRequest);
        return ApiResponses.<UserDTO>builder()
                .data(userDTO)
                .success(true)
                .code(201)
                .message("User created successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }

    @Operation(summary = "Lấy thông tin người dùng",
            description = "API này sẽ trả về thông tin người dùng trong hệ thống.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Thông tin người dùng")
            })
    @PreAuthorize("hasPermission(null, 'user.read')")
    @GetMapping("/users/my-info")
    public ApiResponses<UserDTO> getMyInfo(@Parameter(description = "Tên người dùng", required = true)
                                                    @RequestParam String username) {
        UserDTO userDTO = userQueryService.getUserInfo(username);
        return ApiResponses.<UserDTO>builder()
                .data(userDTO)
                .success(true)
                .code(200)
                .message("Get my info successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }

    @Operation(summary = "Cập nhật thông tin người dùng",
            description = "API này sẽ trả cập nhật thông tin người dùng trong hệ thống và trả về thông tin.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Thông tin của người dùng cần cập nhật",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UpdateUserRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Thông tin người dùng sau khi cập nhật")
            })
    @PreAuthorize("hasPermission(null, 'user.update')")
    @PutMapping("/users")
    public ApiResponses<UserDTO> updateUser(@Parameter(description = "username of user account", required = true)
                                                     @RequestParam String username,
                                            @RequestBody UpdateUserRequest updateUserRequest) {
        UserDTO userDTO = userCommandService.updateUser(username, updateUserRequest);
        return ApiResponses.<UserDTO>builder()
                .data(userDTO)
                .success(true)
                .code(200)
                .message("Update user successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }

    @Operation(summary = "Tìm kiếm người dùng",
            description = "API này sẽ trả về danh sách người dùng theo từ khóa tìm kiếm.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Thông tin tìm kiếm",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = SearchUserRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Danh sách người dùng")
            })
    @PreAuthorize("hasPermission(null, 'user.manage')")
    @GetMapping("/users/search")
    public PageApiResponse<List<UserDTO>> search(@RequestBody SearchUserRequest searchUserRequest) {
        Long totalUsers = userQueryService.totalUsers(searchUserRequest);
        List<UserDTO> userDTOS = Collections.emptyList();
        if(totalUsers != 0) {
            userDTOS = userQueryService.search(searchUserRequest);
        }
        PageApiResponse.PageableResponse pageableResponse = PageApiResponse.PageableResponse.builder()
                .pageSize(searchUserRequest.getPageSize())
                .pageIndex(searchUserRequest.getPageIndex())
                .totalElements(totalUsers)
                .totalPages((int)(Math.ceil((double)totalUsers / searchUserRequest.getPageSize())))
                .hasNext(searchUserRequest.getPageIndex() + searchUserRequest.getPageSize() < totalUsers)
                .hasPrevious(searchUserRequest.getPageIndex() > 1).build();

        return PageApiResponse.<List<UserDTO>>builder()
                .data(userDTOS)
                .pageable(pageableResponse)
                .success(true)
                .code(200)
                .message("Search user successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }


    @Operation(summary = "Khóa/Mở khóa người dùng",
            description = "API này sẽ khoá hoặc mở khóa một người dùng trong hệ thống.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Khóa/Mở khóa người dùng thành công")
            })
    @PreAuthorize("hasPermission(null, 'user.delete')")
    @PatchMapping("/users/lock")
    public ApiResponses<Void> lockUser(@Parameter(description = "ID của người dùng cần cập nhật trạng thái", example = "12345")
                                           @RequestParam String username,
                                       @Parameter(description = "Trạng thái của người dùng: `true` để khoá, `false` để mở khoá", example = "true")
                                           @RequestParam boolean enabled) {
        userCommandService.lockUser(username, enabled);
        return ApiResponses.<Void>builder()
                .success(true)
                .code(200)
                .message("Lock/Unlock user successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }

    @Operation(summary = "Đổi mật khẩu",
            description = "API này sẽ đổi mật khẩu của người dùng.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Thông tin mật khẩu",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ChangePasswordRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Mật khẩu đã được đổi")
            })
    @PreAuthorize("hasPermission(null, 'user.update')")
    @PatchMapping("/users/change-password")
    public ApiResponses<Void> changePassword(@Parameter(description = "username of user account", required = true)
                                                 @RequestParam String username,
                                             @RequestBody ChangePasswordRequest changePasswordRequest) {
        userCommandService.changePassword(username, changePasswordRequest);
        return ApiResponses.<Void>builder()
                .success(true)
                .code(200)
                .message("Password successfully changed")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }


    @Operation(
            summary = "Đổi avatar",
            description = "API này giành cho người dùng đổi avatar.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Tệp hình ảnh để đổi avatar.",
                    required = true,
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(type = "string", format = "binary", name = "files")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Avatar đã được đổi thành công")
            })
    @PutMapping("/users/avatar")
    public ApiResponses<UUID> changeAvatar(@Parameter(description = "Tên tài khoản", required = true)
                                                  @RequestParam String username,
                                              @Parameter(description = "Tệp hình ảnh", required = true)
                                                  @RequestPart List<MultipartFile> files) {
        UUID avatarId = userCommandService.changeAvatar(username, files);
        return ApiResponses.<UUID>builder()
                .data(avatarId)
                .success(true)
                .code(200)
                .message("Avatar successfully changed")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }

    @GetMapping("/users/export")
    public ApiResponses<Void> exportUserListToExcel(@RequestBody SearchUserRequest searchUserRequest) {
        userQueryService.exportUserListToExcel(searchUserRequest);
        return ApiResponses.<Void>builder()
                .success(true)
                .code(200)
                .message("Export user list to excel successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }

    @PostMapping("/users/import")
    public ApiResponses<List<UserDTO>> importUserFile(@RequestParam MultipartFile file){
        List<UserDTO> userResponse = userCommandService.importUserFile(file);
        return ApiResponses.<List<UserDTO>>builder()
                .data(userResponse)
                .success(true)
                .code(200)
                .message("Import user file successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }

    @GetMapping("/test-retry")
    public ApiResponses<Void> testRetry() {
        return fileService.testRetry();
    }
}
