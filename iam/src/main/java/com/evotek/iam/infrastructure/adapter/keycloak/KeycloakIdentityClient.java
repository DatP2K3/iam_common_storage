package com.evotek.iam.infrastructure.adapter.keycloak;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.evotek.iam.application.dto.request.*;
import com.evotek.iam.application.dto.response.TokenDTO;
import com.evotek.iam.domain.command.ResetKeycloakPasswordCmd;
import com.evotek.iam.infrastructure.adapter.keycloak.config.KeycloakIdentityClientConfiguration;

import feign.QueryMap;

@FeignClient(
        name = "identity-client",
        url = "${idp.url}",
        contextId = "identity-client",
        configuration = KeycloakIdentityClientConfiguration.class)
public interface KeycloakIdentityClient {
    @PostMapping(
            value = "/realms/IamService/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenDTO getToken(@QueryMap GetTokenRequest param);

    @PostMapping(value = "/admin/realms/IamService/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> createUser(
            @RequestHeader("authorization") String token, @RequestBody CreateUserKeycloakRequest param);

    @PostMapping(
            value = "/realms/IamService/protocol/openid-connect/logout",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenDTO logout(@RequestHeader("authorization") String token, @QueryMap LogoutRequest logoutRequest);

    @PostMapping(
            value = "/realms/IamService/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenDTO refreshToken(@QueryMap RefreshTokenRequest refreshTokenRequest);

    @PutMapping(value = "/admin/realms/IamService/users/{user_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateUser(
            @RequestHeader("authorization") String token,
            @PathVariable("user_id") String userId,
            @RequestBody UpdateUserRequest updateUserRequest);

    @PutMapping(value = "/admin/realms/IamService/users/{user_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    void lockUser(
            @RequestHeader("authorization") String token,
            @PathVariable("user_id") String userId,
            @RequestBody LockUserRequest lockUserRequest);

    @PutMapping(
            value = "/admin/realms/IamService/users/{user_id}/reset-password",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    void resetPassword(
            @RequestHeader("authorization") String token,
            @PathVariable("user_id") String userId,
            @RequestBody ResetKeycloakPasswordCmd resetKeycloakPasswordCmd);
}
