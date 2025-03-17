package com.evotek.iam.infrastructure.domainrepository;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.evo.common.repository.AbstractDomainRepository;
import com.evotek.iam.domain.Permission;
import com.evotek.iam.domain.query.SearchPermissionQuery;
import com.evotek.iam.domain.repository.PermissionDomainRepository;
import com.evotek.iam.infrastructure.persistence.entity.PermissionEntity;
import com.evotek.iam.infrastructure.persistence.mapper.PermissionEntityMapper;
import com.evotek.iam.infrastructure.persistence.repository.PermissionEntityRepository;
import com.evotek.iam.infrastructure.support.exception.AppErrorCode;
import com.evotek.iam.infrastructure.support.exception.AppException;

@Repository
public class PermissionDomainRepositoryImpl extends AbstractDomainRepository<Permission, PermissionEntity, UUID>
        implements PermissionDomainRepository {
    private final PermissionEntityMapper permissionEntityMapper;
    private final PermissionEntityRepository permissionEntityRepository;

    public PermissionDomainRepositoryImpl(
            PermissionEntityRepository permissionEntityRepository, PermissionEntityMapper permissionEntityMapper) {
        super(permissionEntityRepository, permissionEntityMapper);
        this.permissionEntityRepository = permissionEntityRepository;
        this.permissionEntityMapper = permissionEntityMapper;
    }

    @Override
    public List<Permission> findPermissionByRoleId(UUID roleId) {
        List<PermissionEntity> permissionEntities = permissionEntityRepository.findPermissionByRoleId(roleId);
        return permissionEntityMapper.toDomainModelList(permissionEntities);
    }

    @Override
    public List<Permission> search(SearchPermissionQuery searchPermissionQuery) {
        List<PermissionEntity> permissionEntities = permissionEntityRepository.search(searchPermissionQuery);
        return permissionEntities.stream()
                .map(permissionEntityMapper::toDomainModel)
                .toList();
    }

    @Override
    public Long count(SearchPermissionQuery searchPermissionQuery) {
        return permissionEntityRepository.count(searchPermissionQuery);
    }

    @Override
    public Permission getById(UUID uuid) {
        return permissionEntityMapper.toDomainModel(permissionEntityRepository
                .findById(uuid)
                .orElseThrow(() -> new AppException(AppErrorCode.PERMISSION_NOT_FOUND)));
    }
}
