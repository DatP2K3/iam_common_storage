package com.evotek.iam.application.service.impl.command;

import com.evotek.iam.application.dto.mapper.PermissionDTOMapper;
import com.evotek.iam.application.dto.request.CreateOrUpdatePermissionRequest;
import com.evotek.iam.application.dto.response.PermissionDTO;
import com.evotek.iam.application.mapper.CommandMapper;
import com.evotek.iam.application.service.PermissionCommandService;
import com.evotek.iam.domain.Permission;
import com.evotek.iam.domain.UserActivityLog;
import com.evotek.iam.domain.command.CreateOrUpdatePermissionCmd;
import com.evotek.iam.domain.command.WriteLogCmd;
import com.evotek.iam.domain.repository.UserActivityLogDomainRepository;
import com.evotek.iam.infrastructure.domainrepository.PermissionDomainRepositoryImpl;
import com.evotek.iam.infrastructure.support.exception.AppErrorCode;
import com.evotek.iam.infrastructure.support.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionCommandServiceImpl implements PermissionCommandService {
    private final PermissionDomainRepositoryImpl permissionDomainRepositoryImpl;
    private final PermissionDTOMapper permissionDTOMapper;
    private final CommandMapper commandMapper;
    private final UserActivityLogDomainRepository userActivityLogDomainRepository;

    @Override
    public PermissionDTO createPermission(CreateOrUpdatePermissionRequest request) {
        CreateOrUpdatePermissionCmd cmd = commandMapper.from(request);
        Permission permission = new Permission(cmd);

        WriteLogCmd logCmd = commandMapper.from("Create permission");
        UserActivityLog userActivityLog = new UserActivityLog(logCmd);
        userActivityLogDomainRepository.save(userActivityLog);
        return permissionDTOMapper.domainModelToDTO(permissionDomainRepositoryImpl.save(permission));
    }

    @Override
    public PermissionDTO updatePermission(CreateOrUpdatePermissionRequest request) {
        CreateOrUpdatePermissionCmd cmd = commandMapper.from(request);
        Permission permission = permissionDomainRepositoryImpl.findById(cmd.getId()).orElseThrow(() -> new AppException(AppErrorCode.PERMISSION_NOT_FOUND));
        permission.update(cmd);

        WriteLogCmd logCmd = commandMapper.from("Update permission");
        UserActivityLog userActivityLog = new UserActivityLog(logCmd);
        userActivityLogDomainRepository.save(userActivityLog);
        return permissionDTOMapper.domainModelToDTO(permissionDomainRepositoryImpl.save(permission));
    }
}
