package com.evotek.notification.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;

import com.evotek.notification.domain.DeviceRegistration;
import com.evotek.notification.infrastructure.persistence.entity.DeviceRegistrationEntity;

@Mapper(componentModel = "Spring")
public interface DeviceRegistrationEntityMapper extends EntityMapper<DeviceRegistration, DeviceRegistrationEntity> {}
