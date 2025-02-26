package com.evotek.iam.application.service.impl.command;

import com.evotek.iam.application.dto.mapper.RoleDTOMapper;
import com.evotek.iam.application.dto.request.CreateOrUpdateRoleRequest;
import com.evotek.iam.application.dto.response.RoleDTO;
import com.evotek.iam.application.mapper.CommandMapper;
import com.evotek.iam.application.service.RoleCommandService;
import com.evotek.iam.domain.Role;
import com.evotek.iam.domain.UserActivityLog;
import com.evotek.iam.domain.command.*;
import com.evotek.iam.domain.repository.RoleDomainRepository;
import com.evotek.iam.domain.repository.UserActivityLogDomainRepository;
import com.evotek.iam.infrastructure.support.exception.AppErrorCode;
import com.evotek.iam.infrastructure.support.exception.AppException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RoleCommandServiceImpl implements RoleCommandService {
    private final RoleDTOMapper roleDTOMapper;
    private final CommandMapper commandMapper;
    private final RoleDomainRepository roleDomainRepository;
    private  final UserActivityLogDomainRepository userActivityLogDomainRepository;

    @Override
    @Transactional
    public RoleDTO createRole(CreateOrUpdateRoleRequest createOrUpdateRoleRequest) {
        CreateOrUpdateRoleCmd createOrUpdateRoleCmd = commandMapper.from(createOrUpdateRoleRequest);
        Role role = new Role(createOrUpdateRoleCmd);
        role = roleDomainRepository.save(role);

        WriteLogCmd logCmd = commandMapper.from("Create role");
        UserActivityLog userActivityLog = new UserActivityLog(logCmd);
        userActivityLogDomainRepository.save(userActivityLog);
        return roleDTOMapper.domainModelToDTO(role);
    }

    @Override
    @Transactional
    public RoleDTO updateRole(CreateOrUpdateRoleRequest createOrUpdateRoleRequest) {
        CreateOrUpdateRoleCmd createOrUpdateRoleCmd = commandMapper.from(createOrUpdateRoleRequest);
        Role role = roleDomainRepository.findById(createOrUpdateRoleCmd.getId()).orElseThrow(() -> new AppException(AppErrorCode.ROLE_NOT_FOUND));
        role.update(createOrUpdateRoleCmd);
        role = roleDomainRepository.save(role);

        WriteLogCmd logCmd = commandMapper.from("Update role");
        UserActivityLog userActivityLog = new UserActivityLog(logCmd);
        userActivityLogDomainRepository.save(userActivityLog);
        return roleDTOMapper.domainModelToDTO(role);
    }
}
