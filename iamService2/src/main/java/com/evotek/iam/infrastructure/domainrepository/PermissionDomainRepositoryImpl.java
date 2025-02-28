package com.evotek.iam.infrastructure.domainrepository;

import com.evotek.iam.domain.Permission;
import com.evotek.iam.domain.query.SearchPermissionQuery;
import com.evotek.iam.domain.repository.PermissionDomainRepository;
import com.evotek.iam.infrastructure.persistence.entity.PermissionEntity;
import com.evotek.iam.infrastructure.persistence.mapper.PermissionEntityMapper;
import com.evotek.iam.infrastructure.persistence.repository.PermissionEntityRepository;
import com.evotek.iam.infrastructure.support.exception.AppErrorCode;
import com.evotek.iam.infrastructure.support.exception.AppException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class PermissionDomainRepositoryImpl extends AbstractDomainRepository<Permission, PermissionEntity, UUID>
        implements PermissionDomainRepository {
    private final PermissionEntityMapper entityMapper;
    private final PermissionEntityRepository repository;

    public PermissionDomainRepositoryImpl(PermissionEntityRepository repository, PermissionEntityMapper entityMapper) {
        super(repository, entityMapper);
        this.repository = repository;
        this.entityMapper = entityMapper;
    }

    @Override
    public List<Permission> findPermissionByRoleId(UUID roleId) {
        List<PermissionEntity> permissionEntities = repository.findPermissionByRoleId(roleId);
        return entityMapper.toDomainModelList(permissionEntities);
    }

    @Override
    public List<Permission> search(SearchPermissionQuery searchPermissionQuery) {
        List<PermissionEntity> permissionEntities = repository.search(searchPermissionQuery);
        return permissionEntities.stream().map(entityMapper::toDomainModel).toList();
    }

    @Override
    public Long count(SearchPermissionQuery searchPermissionQuery) {
        return repository.count(searchPermissionQuery);
    }

    @Override
    public Permission getById(UUID uuid) {
        return entityMapper.toDomainModel(repository.findById(uuid).orElseThrow(() -> new AppException(AppErrorCode.PERMISSION_NOT_FOUND)));
    }
}