package com.evotek.notification.presentation.rest;

import com.evo.common.dto.response.ApiResponses;
import com.evotek.notification.application.dto.request.RegisterOrUpdateDeviceRequest;
import com.evotek.notification.application.service.push.impl.command.UserTopicCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

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

        @PostMapping("/user-token/{userId}/update")
        public ApiResponses<Void> updateTopicOfUser(RegisterOrUpdateDeviceRequest registerOrUpdateDeviceRequest) {
            userTopicCommandService.updateTopicOfUser(registerOrUpdateDeviceRequest);
            return ApiResponses.<Void>builder()
                    .success(true)
                    .code(201)
                    .message("Update topic of user success")
                    .timestamp(System.currentTimeMillis())
                    .status("OK")
                    .build();
        }
}
