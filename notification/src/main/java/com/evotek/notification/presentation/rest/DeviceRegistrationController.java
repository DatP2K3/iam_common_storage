package com.evotek.notification.presentation.rest;

import org.springframework.web.bind.annotation.*;

import com.evo.common.dto.response.ApiResponses;
import com.evotek.notification.application.dto.request.RegisterOrUpdateDeviceRequest;
import com.evotek.notification.application.dto.request.UnRegisterDeviceRequest;
import com.evotek.notification.application.service.push.impl.command.DeviceRegistrationCommandService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/device-registration")
@RequiredArgsConstructor
public class DeviceRegistrationController {
    private final DeviceRegistrationCommandService deviceRegistrationCommandService;

    @PostMapping("/register")
    public ApiResponses<Void> registerDevice(@RequestBody RegisterOrUpdateDeviceRequest request) {
        deviceRegistrationCommandService.registerDevice(request);
        return ApiResponses.<Void>builder()
                .success(true)
                .code(201)
                .message("Đăng ký thiết bị cho fcm thành công")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }

    @DeleteMapping("/unregister")
    public ApiResponses<Void> unRegisterDevice(@RequestBody UnRegisterDeviceRequest unRegisterDeviceRequest) {
        deviceRegistrationCommandService.unregisterDevice(unRegisterDeviceRequest);
        return ApiResponses.<Void>builder()
                .success(true)
                .code(201)
                .message("Huỷ đăng ký thiết bị cho fcm thành công")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }
}
