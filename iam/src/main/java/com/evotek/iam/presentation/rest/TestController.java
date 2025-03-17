package com.evotek.iam.presentation.rest;

import org.springframework.web.bind.annotation.*;

import com.evo.common.dto.event.PushNotificationEvent;
import com.evo.common.dto.response.ApiResponses;
import com.evotek.iam.application.service.UserCommandService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {
    private final UserCommandService userCommandService;

    @GetMapping("/fcm")
    public ApiResponses<Void> testTopic(@RequestBody PushNotificationEvent pushNotificationEvent) {
        userCommandService.testFcm(pushNotificationEvent);
        return ApiResponses.<Void>builder()
                .success(true)
                .code(200)
                .message("Test retry successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }
}
