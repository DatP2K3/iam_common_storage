package com.evotek.notification.domain.repository;

import java.util.UUID;

import com.evo.common.repository.DomainRepository;
import com.evotek.notification.domain.Notification;

public interface NotificationDomainRepository extends DomainRepository<Notification, UUID> {}
