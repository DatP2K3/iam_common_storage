package com.evotek.iam.application.service;

import com.evo.common.UserAuthority;
import com.evo.common.dto.response.PageApiResponse;
import com.evotek.iam.application.dto.request.SearchUserRequest;
import com.evotek.iam.application.dto.response.UserDTO;

import java.util.List;

public interface UserQueryService {
    UserDTO getUserInfo(String username);
    Long totalUsers(SearchUserRequest request);
    List<UserDTO> search(SearchUserRequest searchUserRequest);
    void exportUserListToExcel(SearchUserRequest searchUserRequest);
    UserAuthority getUserAuthority(String username);
    UserAuthority getClientAuthority(String clientId);
}
