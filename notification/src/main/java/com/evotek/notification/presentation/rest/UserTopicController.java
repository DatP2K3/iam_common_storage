package com.evotek.notification.presentation.rest;

import java.util.UUID;

import org.springframework.web.bind.annotation.*;

import com.evo.common.dto.request.UpdateTopicsOfUserRequest;
import com.evo.common.dto.response.ApiResponses;
import com.evotek.notification.application.service.push.impl.command.UserTopicCommandService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserTopicController {
    private final UserTopicCommandService userTopicCommandService;

    @PostMapping("/user-token/{userId}")
    public ApiResponses<Void> initUserTopic(@PathVariable UUID userId) {
        userTopicCommandService.initUserTopic(userId);
        return ApiResponses.<Void>builder()
                .success(true)
                .code(201)
                .message("Init user topic success")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }

    @PutMapping("/user-token/update")
    public ApiResponses<Void> updateTopicOfUser(@RequestBody UpdateTopicsOfUserRequest updateTopicsOfUserRequest) {
        userTopicCommandService.updateTopicOfUser(updateTopicsOfUserRequest);
        return ApiResponses.<Void>builder()
                .success(true)
                .code(201)
                .message("Update topic of user success")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }
}
