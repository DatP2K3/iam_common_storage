package com.evotek.iam.application.service.impl.query;

import java.util.List;

import org.springframework.stereotype.Service;

import com.evotek.iam.application.dto.mapper.PermissionDTOMapper;
import com.evotek.iam.application.dto.request.SearchPermissionRequest;
import com.evotek.iam.application.dto.response.PermissionDTO;
import com.evotek.iam.application.mapper.QueryMapper;
import com.evotek.iam.application.service.PermissionQueryService;
import com.evotek.iam.domain.Permission;
import com.evotek.iam.domain.query.SearchPermissionQuery;
import com.evotek.iam.domain.repository.PermissionDomainRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PermissionQueryServiceImpl implements PermissionQueryService {
    private final PermissionDomainRepository permissionDomainRepository;
    private final QueryMapper queryMapper;
    private final PermissionDTOMapper permissionDTOMapper;

    @Override
    public List<PermissionDTO> search(SearchPermissionRequest searchPermissionRequest) {
        SearchPermissionQuery searchPermissionQuery = queryMapper.from(searchPermissionRequest);
        List<Permission> permissions = permissionDomainRepository.search(searchPermissionQuery);
        return permissions.stream().map(permissionDTOMapper::domainModelToDTO).toList();
    }

    @Override
    public Long totalPermissions(SearchPermissionRequest searchPermissionRequest) {
        SearchPermissionQuery searchPermissionQuery = queryMapper.from(searchPermissionRequest);
        return permissionDomainRepository.count(searchPermissionQuery);
    }
}
