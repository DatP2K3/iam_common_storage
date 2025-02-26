package com.evotek.iam.application.service.impl.query;

import com.evo.common.UserAuthority;
import com.evo.common.dto.response.PageApiResponse;
import com.evotek.iam.application.dto.mapper.UserDTOMapper;
import com.evotek.iam.application.dto.request.SearchUserRequest;
import com.evotek.iam.application.dto.response.UserDTO;
import com.evotek.iam.application.mapper.QueryMapper;
import com.evotek.iam.application.service.UserQueryService;
import com.evotek.iam.domain.*;
import com.evotek.iam.domain.query.SearchUserQuery;
import com.evotek.iam.domain.repository.OauthClientDomainRepository;
import com.evotek.iam.domain.repository.RoleDomainRepository;
import com.evotek.iam.domain.repository.UserDomainRepository;
import com.evotek.iam.infrastructure.support.exception.AppErrorCode;
import com.evotek.iam.infrastructure.support.exception.AppException;
import com.evotek.iam.infrastructure.support.exception.AuthErrorCode;
import com.evotek.iam.infrastructure.support.exception.AuthException;
import lombok.RequiredArgsConstructor;
import org.jxls.builder.JxlsOutput;
import org.jxls.builder.JxlsOutputFile;
import org.jxls.transform.poi.JxlsPoiTemplateFillerBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {
    private final UserDomainRepository userDomainRepository;
    private final UserDTOMapper userDTOMapper;
    private final QueryMapper queryMapper;
    private final RoleDomainRepository roleDomainRepository;
    private final OauthClientDomainRepository oauthClientDomainRepository;

    @Override
    public UserDTO getUserInfo(String username) {
        User user = userDomainRepository.findByUsername(username);
        return userDTOMapper.domainModelToDTO(user);
    }

    @Override
    public PageApiResponse<List<UserDTO>> search(SearchUserRequest request) {
        SearchUserQuery searchUserQuery = queryMapper.from(request);
        List<User> users = userDomainRepository.search(searchUserQuery);
        Long totalUsers = userDomainRepository.count(searchUserQuery);
        List <UserDTO> userDTOS = users.stream().map(userDTOMapper::domainModelToDTO).toList();
        PageApiResponse.PageableResponse pageableResponse = PageApiResponse.PageableResponse.builder()
                .pageSize(request.getPageSize())
                .pageIndex(request.getPageIndex())
                .totalElements(totalUsers)
                .totalPages((int)(Math.ceil((double)totalUsers / request.getPageSize())))
                .hasNext(request.getPageIndex() + request.getPageSize() < totalUsers)
                .hasPrevious(request.getPageIndex() > 1).build();
        return PageApiResponse.<List<UserDTO>>builder()
                .data(userDTOS)
                .success(true)
                .code(200)
                .pageable(pageableResponse)
                .message("Search user successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }

    @Override
    public void exportUserListToExcel(SearchUserRequest searchUserRequest) {
        PageApiResponse<List<UserDTO>> pageApiResponse = search(searchUserRequest);
        List<User> users = pageApiResponse.getData().stream().map(userDTOMapper::dtoToDomainModel).toList();
        Map<String, Object> data = new HashMap<>();
        data.put("users", users);
        try (InputStream inputStream = new ClassPathResource("templates/user_template.xlsx").getInputStream()) {
            JxlsOutput output = new JxlsOutputFile(new File("exports/output_user_data.xlsx"));
            JxlsPoiTemplateFillerBuilder.newInstance()
                    .withTemplate(inputStream)
                    .build()
                    .fill(data, output);
        } catch (IOException e) {
            throw new AppException(AppErrorCode.FILE_NOT_FOUND);
        }
    }

    @Override
    public UserAuthority getUserAuthority(String username) {
        User user = userDomainRepository.findByUsername(username);
        UserRole userRole = user.getUserRole();
        Role role = roleDomainRepository.findById(userRole.getRoleId()).orElseThrow(() -> new AuthException(AuthErrorCode.ROLE_NOT_EXISTED));
        boolean isRoot = role.isRoot();
        List<Permission> permissions = roleDomainRepository.findPermissionByRoleId(userRole.getRoleId());
        List<String> grantedPermissions = List.of();
        if (permissions != null && !permissions.isEmpty()) {
            grantedPermissions = permissions.stream()
                    .filter(Objects::nonNull) // Lọc các phần tử null nếu có
                    .map(permission -> permission.getResourceId() + "." + permission.getScope())
                    .toList();
        }
        return UserAuthority.builder().userId(user.getSelfUserID()).isRoot(isRoot).isClient(false).grantedPermissions(grantedPermissions).build();
    }

    @Override
    public UserAuthority getClientAuthority(String clientId) {
        OauthClient oauthClient = oauthClientDomainRepository.findByClientId(clientId);
        return UserAuthority.builder().userId(oauthClient.getId()).isRoot(false).isClient(true).build();
    }
}
