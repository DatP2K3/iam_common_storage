package com.evotek.iam.domain.repository;

import com.evotek.iam.domain.UserActivityLog;
import com.evotek.iam.infrastructure.domainrepository.DomainRepository;

import java.util.UUID;

public interface UserActivityLogDomainRepository extends DomainRepository<UserActivityLog, UUID> {
}
