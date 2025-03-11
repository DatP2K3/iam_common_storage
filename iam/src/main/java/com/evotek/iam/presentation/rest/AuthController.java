package com.evotek.iam.presentation.rest;

import java.util.Map;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import com.evo.common.dto.response.ApiResponses;
import com.evotek.iam.application.configuration.AuthProperties;
import com.evotek.iam.application.configuration.TokenProvider;
import com.evotek.iam.application.dto.request.ClientTokenRequest;
import com.evotek.iam.application.dto.request.LoginRequest;
import com.evotek.iam.application.dto.request.VerifyOtpRequest;
import com.evotek.iam.application.dto.response.TokenDTO;
import com.evotek.iam.application.service.AuthServiceCommand;
import com.evotek.iam.application.service.AuthServiceQuery;
import com.evotek.iam.application.service.ServiceStrategy;
import com.evotek.iam.application.service.impl.command.SelfIDPAuthCommandServiceImpl;
import com.evotek.iam.domain.command.ResetKeycloakPasswordCmd;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RefreshScope
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private String typeAuthCommandService = "keycloakAuthCommandService";
    private String typeAuthQueryService = "keycloakAuthQueryService";
    private final ServiceStrategy serviceStrategy;
    private AuthServiceCommand authServiceCommand;
    private AuthServiceQuery authServiceQuery;
    private final TokenProvider tokenProvider;
    private final AuthProperties authProperties;

    @PostConstruct
    public void init() {
        if (!authProperties.isKeycloakEnabled()) {
            this.typeAuthCommandService = "selfIdpAuthCommandService";
            this.typeAuthQueryService = "selfIdpAuthQueryService";
        }
        this.authServiceCommand = serviceStrategy.getAuthServiceCommand(typeAuthCommandService);
        this.authServiceQuery = serviceStrategy.getAuthServiceQuery(typeAuthQueryService);
    }

    @GetMapping("/test/refresh")
    public boolean testRefresh() {
        return authProperties.isKeycloakEnabled();
    }

    @GetMapping("/certificate/.well-known/jwks.json")
    Map<String, Object> keys() {
        return tokenProvider.jwkSet().toJSONObject();
    }

    @Operation(
            summary = "Đăng nhập",
            description = "API này sẽ xác thực thông tin đăng nhập và chuyển sang xác thực 2 bước bằng mã Otp.",
            requestBody =
                    @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            description = "Thông tin đăng nhập của người dùng",
                            required = true,
                            content =
                                    @io.swagger.v3.oas.annotations.media.Content(
                                            schema = @Schema(implementation = LoginRequest.class))),
            responses = {@ApiResponse(responseCode = "201", description = "Gửi mã Otp thành công")})
    @PostMapping("/auth/login_iam")
    public ApiResponses<TokenDTO> loginIam(@RequestBody LoginRequest loginRequest) {
        TokenDTO tokenDTO = authServiceCommand.authenticate(loginRequest);
        return ApiResponses.<TokenDTO>builder()
                .data(tokenDTO)
                .success(true)
                .code(201)
                .message(authProperties.isKeycloakEnabled() ? "Login successfully" : "OTP sent to your Email")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }

    @Operation(
            summary = "Xác thực mã Otp",
            description = "API này sẽ xác thực mã Otp và trả về token.",
            requestBody =
                    @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            description = "Thông tin xác thực Otp",
                            required = true,
                            content =
                                    @io.swagger.v3.oas.annotations.media.Content(
                                            schema = @Schema(implementation = VerifyOtpRequest.class))),
            responses = {@ApiResponse(responseCode = "200", description = "OTP đã được xác nhận thành công")})
    @PostMapping("/auth/verify-otp")
    public ApiResponses<TokenDTO> verifyOtp(@RequestBody VerifyOtpRequest verifyOtpRequest) {
        TokenDTO result = ((SelfIDPAuthCommandServiceImpl) authServiceCommand).verifyOtp(verifyOtpRequest);
        return ApiResponses.<TokenDTO>builder()
                .data(result)
                .success(true)
                .code(200)
                .message("OTP đã được xác nhận thành công")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }

    @Operation(
            summary = "Đăng xuất",
            description = "API này sẽ đăng xuất người dùng khỏi hệ thống.",
            responses = {@ApiResponse(responseCode = "200", description = "Đăng xuất thành công")})
    @PostMapping("/auth/logout-user")
    ApiResponses<String> logoutIam(
            @Parameter(description = "request", hidden = true) HttpServletRequest request,
            @Parameter(description = "Refresh token từ client", required = true) @RequestParam String refreshToken) {
        authServiceCommand.logoutIam(request, refreshToken);
        return ApiResponses.<String>builder()
                .data("Logout successful")
                .success(true)
                .code(200)
                .message("Logout successful")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }

    @Operation(
            summary = "Refresh Token",
            description = "API này sẽ refresh token và trả về token mới.",
            responses = {@ApiResponse(responseCode = "200", description = "Refresh Token thành công")})
    @PostMapping("/auth/refresh")
    ApiResponses<TokenDTO> refresh(
            @Parameter(description = "Refresh token từ client", required = true) @RequestParam("refreshToken")
                    String refreshToken) {
        TokenDTO tokenDTO = authServiceCommand.refresh(refreshToken);
        return ApiResponses.<TokenDTO>builder()
                .data(tokenDTO)
                .success(true)
                .code(200)
                .message("Refresh Token successful")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }

    @Operation(
            summary = "Yêu cầu reset mật khẩu với Self IDP / Reset mật khẩu với Keycloak",
            description = "API này sẽ gửi link reset mật khẩu đến email với Self IDP / Reset mật khẩu với Keycloak.",
            requestBody =
                    @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            description = "Thông tin mật khẩu",
                            required = true,
                            content =
                                    @io.swagger.v3.oas.annotations.media.Content(
                                            schema = @Schema(implementation = ResetKeycloakPasswordCmd.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Thành công")})
    @PostMapping("/auth/forgot-password")
    public ApiResponses<Void> requestPasswordReset(
            @Parameter(description = "Tên tài khoản của người dùng", required = true) @RequestParam String username,
            @RequestBody(required = false) ResetKeycloakPasswordCmd resetKeycloakPasswordCmd) {
        authServiceCommand.requestPasswordReset(username, resetKeycloakPasswordCmd);
        return ApiResponses.<Void>builder()
                .success(true)
                .code(200)
                .message(
                        authProperties.isKeycloakEnabled()
                                ? "Password successfully reset"
                                : "Reset password link sent to email")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }

    @Operation(
            summary = "Reset mật khẩu",
            description = "API này sẽ reset mật khẩu.",
            requestBody =
                    @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            description = "Thông tin mật khẩu",
                            required = true,
                            content =
                                    @io.swagger.v3.oas.annotations.media.Content(
                                            schema = @Schema(implementation = ResetKeycloakPasswordCmd.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Mật khẩu đã được reset")})
    @PatchMapping("/auth/reset-password")
    public ApiResponses<Void> resetPassword(
            @RequestParam String token, @RequestBody ResetKeycloakPasswordCmd resetKeycloakPasswordCmd) {
        authServiceCommand.resetPassword(token, resetKeycloakPasswordCmd);
        return ApiResponses.<Void>builder()
                .success(true)
                .code(200)
                .message("Password successfully reset")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }

    @PostMapping("/client/token/{clientId}")
    public ApiResponses<String> getClientToken(
            @PathVariable String clientId, @RequestBody ClientTokenRequest clientTokenRequest) {
        String token = authServiceQuery.getClientToken(clientId, clientTokenRequest.getClientSecret());
        return ApiResponses.<String>builder()
                .data(token)
                .success(true)
                .code(200)
                .message("Get client authority successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }
}
