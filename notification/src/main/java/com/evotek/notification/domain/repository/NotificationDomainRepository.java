package com.evotek.notification.domain.repository;

import java.util.UUID;

import com.evotek.notification.domain.Notification;
import com.evotek.notification.infrastructure.domainrepository.DomainRepository;

public interface NotificationDomainRepository extends DomainRepository<Notification, UUID> {}
