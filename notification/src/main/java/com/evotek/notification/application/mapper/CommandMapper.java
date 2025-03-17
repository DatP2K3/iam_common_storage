package com.evotek.notification.application.mapper;

import org.mapstruct.Mapper;

import com.evo.common.dto.event.PushNotificationEvent;
import com.evotek.notification.application.dto.request.RegisterOrUpdateDeviceRequest;
import com.evotek.notification.domain.command.RegisterOrUpdateDeviceCmd;
import com.evotek.notification.domain.command.StoreNotificationCmd;

@Mapper(componentModel = "spring")
public interface CommandMapper {
    StoreNotificationCmd from(PushNotificationEvent pushNotificationEvent);

    RegisterOrUpdateDeviceCmd from(RegisterOrUpdateDeviceRequest registerOrUpdateDeviceRequest);
}
