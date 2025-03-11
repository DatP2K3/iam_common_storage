package com.evotek.notification.infrastructure.domainrepository;

import java.util.*;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.evotek.notification.domain.DeviceRegistration;
import com.evotek.notification.domain.NotificationDelivery;
import com.evotek.notification.domain.repository.DeviceRegistrationDomainRepository;
import com.evotek.notification.infrastructure.persistence.entity.DeviceRegistrationEntity;
import com.evotek.notification.infrastructure.persistence.entity.NotificationDeliveryEntity;
import com.evotek.notification.infrastructure.persistence.mapper.DeviceRegistrationEntityMapper;
import com.evotek.notification.infrastructure.persistence.mapper.NotificationDeliveryEntityMapper;
import com.evotek.notification.infrastructure.persistence.repository.DeviceRegistrationEntityRepository;
import com.evotek.notification.infrastructure.persistence.repository.NotificationDeliveryEntityRepository;
import com.evotek.notification.infrastructure.support.exception.AppErrorCode;
import com.evotek.notification.infrastructure.support.exception.AppException;

@Repository
public class DeviceRegistrationDomainRepositoryImpl
        extends AbstractDomainRepository<DeviceRegistration, DeviceRegistrationEntity, UUID>
        implements DeviceRegistrationDomainRepository {
    private final DeviceRegistrationEntityRepository deviceRegistrationEntityRepository;
    private final DeviceRegistrationEntityMapper deviceRegistrationEntityMapper;
    private final NotificationDeliveryEntityRepository notificationDeliveryEntityRepository;
    private final NotificationDeliveryEntityMapper notificationDeliveryEntityMapper;

    public DeviceRegistrationDomainRepositoryImpl(
            DeviceRegistrationEntityRepository deviceRegistrationEntityRepository,
            DeviceRegistrationEntityMapper deviceRegistrationEntityMapper,
            NotificationDeliveryEntityRepository notificationDeliveryEntityRepository,
            NotificationDeliveryEntityMapper notificationDeliveryEntityMapper) {
        super(deviceRegistrationEntityRepository, deviceRegistrationEntityMapper);
        this.deviceRegistrationEntityRepository = deviceRegistrationEntityRepository;
        this.deviceRegistrationEntityMapper = deviceRegistrationEntityMapper;
        this.notificationDeliveryEntityRepository = notificationDeliveryEntityRepository;
        this.notificationDeliveryEntityMapper = notificationDeliveryEntityMapper;
    }

    @Override
    @Transactional
    public DeviceRegistration save(DeviceRegistration deviceRegistration) {

        DeviceRegistrationEntity deviceRegistrationEntity = deviceRegistrationEntityMapper.toEntity(deviceRegistration);
        List<NotificationDelivery> notificationDeliveries = deviceRegistration.getNotificationDeliveries();
        List<NotificationDeliveryEntity> notificationDeliveryEntities =
                notificationDeliveryEntityMapper.toEntityList(notificationDeliveries);
        if (notificationDeliveryEntities != null && !notificationDeliveryEntities.isEmpty()) {
            notificationDeliveryEntityRepository.saveAll(notificationDeliveryEntities);
        }
        deviceRegistrationEntity = deviceRegistrationEntityRepository.save(deviceRegistrationEntity);
        return this.enrich(deviceRegistrationEntityMapper.toDomainModel(deviceRegistrationEntity));
    }

    @Override
    public DeviceRegistration getById(UUID uuid) {
        DeviceRegistrationEntity deviceRegistrationEntity = deviceRegistrationEntityRepository
                .findById(uuid)
                .orElseThrow(() -> new AppException(AppErrorCode.DEVICE_REGISTRATION_NOT_FOUND));
        return this.enrich(deviceRegistrationEntityMapper.toDomainModel(deviceRegistrationEntity));
    }

    @Override
    public List<DeviceRegistration> findByUserIdAndEnabled(UUID userId) {
        List<DeviceRegistrationEntity> deviceRegistrationEntities =
                deviceRegistrationEntityRepository.findByUserIdAndEnabledTrue(userId);
        return this.enrichList(deviceRegistrationEntityMapper.toDomainModelList(deviceRegistrationEntities));
    }

    @Override
    public List<DeviceRegistration> findByDeviceTokenAndEnabled(String deviceToken) {
        List<DeviceRegistrationEntity> deviceRegistrationEntities =
                deviceRegistrationEntityRepository.findByDeviceTokenAndEnabledTrue(deviceToken);
        return this.enrichList(deviceRegistrationEntityMapper.toDomainModelList(deviceRegistrationEntities));
    }

    @Override
    public List<DeviceRegistration> findByTopic(String topic) {
        List<DeviceRegistrationEntity> deviceRegistrationEntities =
                deviceRegistrationEntityRepository.findByTopic(topic);
        return this.enrichList(deviceRegistrationEntityMapper.toDomainModelList(deviceRegistrationEntities));
    }

    @Override
    public DeviceRegistration findByDeviceIdAndUserId(UUID deviceId, UUID userId) {
        DeviceRegistrationEntity deviceRegistrationEntity = deviceRegistrationEntityRepository
                .findByDeviceIdAndUserId(deviceId, userId)
                .orElse(null);
        if (deviceRegistrationEntity != null) {
            return this.enrich(deviceRegistrationEntityMapper.toDomainModel(deviceRegistrationEntity));
        }
        return null;
    }

    @Override
    protected List<DeviceRegistration> enrichList(List<DeviceRegistration> deviceRegistrations) {
        if (deviceRegistrations.isEmpty()) return deviceRegistrations;

        List<UUID> deviceIds =
                deviceRegistrations.stream().map(DeviceRegistration::getId).toList();
        Map<UUID, List<NotificationDelivery>> deviceMap =
                notificationDeliveryEntityRepository.findByDeviceRegistrationIdIn(deviceIds).stream()
                        .collect(Collectors.groupingBy(
                                NotificationDeliveryEntity::getDeviceRegistrationId,
                                Collectors.mapping(
                                        notificationDeliveryEntityMapper::toDomainModel, Collectors.toList())));

        deviceRegistrations.forEach(deviceRegistration -> deviceRegistration.setNotificationDeliveries(
                new ArrayList<>(deviceMap.getOrDefault(deviceRegistration.getId(), Collections.emptyList()))));
        return deviceRegistrations;
    }
}
