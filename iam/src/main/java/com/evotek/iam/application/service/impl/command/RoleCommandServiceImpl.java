package com.evotek.iam.application.service.impl.command;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.evotek.iam.application.dto.mapper.RoleDTOMapper;
import com.evotek.iam.application.dto.request.CreateOrUpdateRoleRequest;
import com.evotek.iam.application.dto.response.RoleDTO;
import com.evotek.iam.application.mapper.CommandMapper;
import com.evotek.iam.application.service.RoleCommandService;
import com.evotek.iam.domain.Role;
import com.evotek.iam.domain.command.*;
import com.evotek.iam.domain.repository.RoleDomainRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleCommandServiceImpl implements RoleCommandService {
    private final RoleDTOMapper roleDTOMapper;
    private final CommandMapper commandMapper;
    private final RoleDomainRepository roleDomainRepository;

    @Override
    @Transactional
    public RoleDTO createRole(CreateOrUpdateRoleRequest createOrUpdateRoleRequest) {
        CreateOrUpdateRoleCmd createOrUpdateRoleCmd = commandMapper.from(createOrUpdateRoleRequest);
        Role role = new Role(createOrUpdateRoleCmd);
        role = roleDomainRepository.save(role);
        return roleDTOMapper.domainModelToDTO(role);
    }

    @Override
    @Transactional
    public RoleDTO updateRole(CreateOrUpdateRoleRequest createOrUpdateRoleRequest) {
        CreateOrUpdateRoleCmd createOrUpdateRoleCmd = commandMapper.from(createOrUpdateRoleRequest);
        Role role = roleDomainRepository.getById(createOrUpdateRoleCmd.getId());
        role.update(createOrUpdateRoleCmd);
        role = roleDomainRepository.save(role);
        return roleDTOMapper.domainModelToDTO(role);
    }
}
