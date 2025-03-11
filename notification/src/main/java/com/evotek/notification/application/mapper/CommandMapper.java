package com.evotek.notification.application.mapper;

import org.mapstruct.Mapper;

import com.evo.common.dto.request.PushNotificationRequest;
import com.evotek.notification.application.dto.request.RegisterOrUpdateDeviceRequest;
import com.evotek.notification.domain.command.RegisterOrUpdateDeviceCmd;
import com.evotek.notification.domain.command.StoreNotificationCmd;

@Mapper(componentModel = "spring")
public interface CommandMapper {
    StoreNotificationCmd from(PushNotificationRequest pushNotificationRequest);

    RegisterOrUpdateDeviceCmd from(RegisterOrUpdateDeviceRequest registerOrUpdateDeviceRequest);
}
