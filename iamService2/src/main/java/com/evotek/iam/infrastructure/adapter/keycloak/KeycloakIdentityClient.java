package com.evotek.iam.infrastructure.adapter.keycloak;

import com.evotek.iam.application.dto.request.UpdateUserRequest;
import com.evotek.iam.application.dto.request.identityKeycloak.*;
import com.evotek.iam.application.dto.response.TokenDTO;
import com.evotek.iam.domain.command.LockUserCmd;
import com.evotek.iam.domain.command.ResetKeycloakPasswordCmd;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "identity-client", url = "${idp.url}")
public interface KeycloakIdentityClient {
    @PostMapping(value = "/realms/IamService/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenDTO getToken(@QueryMap GetTokenRequest param);

    @PostMapping(value = "/admin/realms/IamService/users",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createUser(
            @RequestHeader("authorization") String token,
            @RequestBody CreateUserKeycloakRequest param);

    @PostMapping(value = "/realms/IamService/protocol/openid-connect/logout",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenDTO logout(
            @RequestHeader("authorization") String token,
            @QueryMap LogoutRequest logoutRequest);

    @PostMapping(value = "/realms/IamService/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenDTO refreshToken(@QueryMap RefreshTokenRequest refreshTokenRequest);

    @PutMapping(value = "/admin/realms/IamService/users/{user_id}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateUser(@RequestHeader("authorization") String token,
                  @PathVariable("user_id") String userId,
                  @RequestBody UpdateUserRequest updateUserRequest);

    @PutMapping(value = "/admin/realms/IamService/users/{user_id}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    void lockUser(@RequestHeader("authorization") String token,
                    @PathVariable("user_id") String userId,
                    @RequestBody LockUserCmd lockUserCmd);

    @PutMapping(value = "/admin/realms/IamService/users/{user_id}/reset-password",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    void resetPassword(@RequestHeader("authorization") String token,
                    @PathVariable("user_id") String userId,
                    @RequestBody ResetKeycloakPasswordCmd resetKeycloakPasswordCmd);

    @GetMapping(value = "/realms/IamService/protocol/openid-connect/userinfo")
    ResponseEntity<Map<String, String>> getUserInfo(@RequestHeader("authorization") String token);
}
