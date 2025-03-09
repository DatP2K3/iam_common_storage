package com.evotek.iam.application.service.impl.command;

import org.springframework.stereotype.Service;

import com.evotek.iam.application.dto.mapper.PermissionDTOMapper;
import com.evotek.iam.application.dto.request.CreateOrUpdatePermissionRequest;
import com.evotek.iam.application.dto.response.PermissionDTO;
import com.evotek.iam.application.mapper.CommandMapper;
import com.evotek.iam.application.service.PermissionCommandService;
import com.evotek.iam.domain.Permission;
import com.evotek.iam.domain.command.CreateOrUpdatePermissionCmd;
import com.evotek.iam.infrastructure.domainrepository.PermissionDomainRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PermissionCommandServiceImpl implements PermissionCommandService {
    private final PermissionDomainRepositoryImpl permissionDomainRepositoryImpl;
    private final PermissionDTOMapper permissionDTOMapper;
    private final CommandMapper commandMapper;

    @Override
    public PermissionDTO createPermission(CreateOrUpdatePermissionRequest request) {
        CreateOrUpdatePermissionCmd cmd = commandMapper.from(request);
        Permission permission = new Permission(cmd);
        return permissionDTOMapper.domainModelToDTO(permissionDomainRepositoryImpl.save(permission));
    }

    @Override
    public PermissionDTO updatePermission(CreateOrUpdatePermissionRequest request) {
        CreateOrUpdatePermissionCmd cmd = commandMapper.from(request);
        Permission permission = permissionDomainRepositoryImpl.getById(cmd.getId());
        permission.update(cmd);
        return permissionDTOMapper.domainModelToDTO(permissionDomainRepositoryImpl.save(permission));
    }
}
