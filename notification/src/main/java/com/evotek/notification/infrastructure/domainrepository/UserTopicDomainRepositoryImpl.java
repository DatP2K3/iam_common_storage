package com.evotek.notification.infrastructure.domainrepository;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.evotek.notification.domain.UserTopic;
import com.evotek.notification.domain.repository.UserTopicDomainRepository;
import com.evotek.notification.infrastructure.persistence.entity.UserTopicEntity;
import com.evotek.notification.infrastructure.persistence.mapper.UserTopicEntityMapper;
import com.evotek.notification.infrastructure.persistence.repository.UserTopicEntityRepository;
import com.evotek.notification.infrastructure.support.exception.AppErrorCode;
import com.evotek.notification.infrastructure.support.exception.AppException;

@Repository
public class UserTopicDomainRepositoryImpl extends AbstractDomainRepository<UserTopic, UserTopicEntity, UUID>
        implements UserTopicDomainRepository {
    private final UserTopicEntityMapper userTopicEntityMapper;
    private final UserTopicEntityRepository userTopicEntityRepository;

    public UserTopicDomainRepositoryImpl(
            UserTopicEntityMapper userTopicEntityMapper, UserTopicEntityRepository userTopicEntityRepository) {
        super(userTopicEntityRepository, userTopicEntityMapper);
        this.userTopicEntityMapper = userTopicEntityMapper;
        this.userTopicEntityRepository = userTopicEntityRepository;
    }

    @Override
    public UserTopic getById(UUID userId) {
        UserTopicEntity userTopicEntity = userTopicEntityRepository
                .findById(userId)
                .orElseThrow(() -> new AppException(AppErrorCode.USER_TOPIC_NOT_FOUND));
        return userTopicEntityMapper.toDomainModel(userTopicEntity);
    }

    @Override
    public List<String> findTopicEnabled(UUID userId) {
        return userTopicEntityRepository.findTopicEnabled(userId);
    }

    @Override
    public List<UserTopic> findByUserIdAndEnabled(UUID userId) {
        List<UserTopicEntity> userTopicEntities = userTopicEntityRepository.findByUserIdAndEnabledTrue(userId);
        return userTopicEntityMapper.toDomainModelList(userTopicEntities);
    }

    @Override
    public List<UserTopic> findByUserId(UUID userId) {
        List<UserTopicEntity> userTopicEntities = userTopicEntityRepository.findByUserId(userId);
        return userTopicEntityMapper.toDomainModelList(userTopicEntities);
    }


}
