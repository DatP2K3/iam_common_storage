package com.evotek.iam.infrastructure.domainrepository;

import com.evotek.iam.domain.UserActivityLog;
import com.evotek.iam.domain.repository.UserActivityLogDomainRepository;
import com.evotek.iam.infrastructure.persistence.entity.UserActivityLogEntity;
import com.evotek.iam.infrastructure.persistence.mapper.UserLogActivityEntityMapper;
import com.evotek.iam.infrastructure.persistence.repository.UserActivityLogEntityRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class UserActivityLogDomainRepositoryImpl extends AbstractDomainRepository<UserActivityLog, UserActivityLogEntity, UUID>
        implements UserActivityLogDomainRepository {

    public UserActivityLogDomainRepositoryImpl(UserActivityLogEntityRepository repository, UserLogActivityEntityMapper entityMapper) {
        super(repository, entityMapper);
    }
}
