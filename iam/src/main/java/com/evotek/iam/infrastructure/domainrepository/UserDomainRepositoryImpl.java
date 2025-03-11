package com.evotek.iam.infrastructure.domainrepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.evotek.iam.domain.User;
import com.evotek.iam.domain.UserActivityLog;
import com.evotek.iam.domain.UserRole;
import com.evotek.iam.domain.query.SearchUserQuery;
import com.evotek.iam.domain.repository.UserDomainRepository;
import com.evotek.iam.infrastructure.persistence.entity.*;
import com.evotek.iam.infrastructure.persistence.mapper.UserActivityLogEntityMapper;
import com.evotek.iam.infrastructure.persistence.mapper.UserEntityMapper;
import com.evotek.iam.infrastructure.persistence.mapper.UserRoleEntityMapper;
import com.evotek.iam.infrastructure.persistence.repository.UserActivityLogEntityRepository;
import com.evotek.iam.infrastructure.persistence.repository.UserEntityRepository;
import com.evotek.iam.infrastructure.persistence.repository.UserRoleEntityRepository;
import com.evotek.iam.infrastructure.support.exception.AppErrorCode;
import com.evotek.iam.infrastructure.support.exception.AppException;
import com.evotek.iam.infrastructure.support.exception.AuthErrorCode;
import com.evotek.iam.infrastructure.support.exception.AuthException;

@Repository
public class UserDomainRepositoryImpl extends AbstractDomainRepository<User, UserEntity, UUID>
        implements UserDomainRepository {
    private final UserEntityMapper userEntityMapper;
    private final UserEntityRepository userEntityRepository;
    private final UserRoleEntityRepository userRoleEntityRepository;
    private final UserRoleEntityMapper userRoleEntityMapper;
    private final UserActivityLogEntityRepository userActivityLogEntityRepository;
    private final UserActivityLogEntityMapper userActivityLogEntityMapper;

    public UserDomainRepositoryImpl(
            UserEntityRepository userEntityRepository,
            UserEntityMapper userEntityMapper,
            UserRoleEntityRepository userRoleEntityRepository,
            UserRoleEntityMapper userRoleEntityMapper,
            UserActivityLogEntityRepository userActivityLogEntityRepository,
            UserActivityLogEntityMapper userActivityLogEntityMapper) {
        super(userEntityRepository, userEntityMapper);
        this.userEntityRepository = userEntityRepository;
        this.userEntityMapper = userEntityMapper;
        this.userRoleEntityRepository = userRoleEntityRepository;
        this.userRoleEntityMapper = userRoleEntityMapper;
        this.userActivityLogEntityRepository = userActivityLogEntityRepository;
        this.userActivityLogEntityMapper = userActivityLogEntityMapper;
    }

    @Override
    public List<User> search(SearchUserQuery query) {
        List<UserEntity> userEntities = userEntityRepository.search(query);
        return this.enrichList(userEntityMapper.toDomainModelList(userEntities));
    }

    @Override
    @Transactional
    public User save(User domainModel) {
        UserEntity userEntity = userEntityMapper.toEntity(domainModel);
        UserRole userRole = domainModel.getUserRole();
        UserRoleEntity userRoleEntity = userRoleEntityMapper.toEntity(userRole);
        userRoleEntityRepository.save(userRoleEntity);
        UserActivityLog userActivityLog = domainModel.getUserActivityLog();
        if (userActivityLog != null) {
            UserActivityLogEntity userActivityLogEntity = userActivityLogEntityMapper.toEntity(userActivityLog);
            userActivityLogEntityRepository.save(userActivityLogEntity);
        }
        return userEntityMapper.toDomainModel(userEntityRepository.save(userEntity));
    }

    @Override
    public User getById(UUID uuid) {
        UserEntity userEntity =
                userEntityRepository.findById(uuid).orElseThrow(() -> new AppException(AppErrorCode.USER_NOT_FOUND));
        return this.enrich(userEntityMapper.toDomainModel(userEntity));
    }

    @Override
    public User getByUsername(String username) {
        UserEntity userEntity = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_EXISTED));
        return this.enrich(userEntityMapper.toDomainModel(userEntity));
    }

    @Override
    protected List<User> enrichList(List<User> users) {
        if (users.isEmpty()) return users;
        List<UUID> userIds = users.stream().map(User::getSelfUserID).toList();
        Map<UUID, UserRole> userRoleMap = userRoleEntityRepository.findByUserIdIn(userIds).stream()
                .map(userRoleEntityMapper::toDomainModel)
                .collect(Collectors.toMap(UserRole::getUserId, userRole -> userRole));
        users.forEach(user -> user.setUserRole(userRoleMap.getOrDefault(user.getSelfUserID(), new UserRole())));
        return users;
    }

    @Override
    public boolean existsByUsername(String username) {
        return userEntityRepository.existsByUsername(username);
    }

    @Override
    public Long count(SearchUserQuery searchUserQuery) {
        return userEntityRepository.count(searchUserQuery);
    }
}
