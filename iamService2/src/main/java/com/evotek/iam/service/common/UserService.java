package com.evotek.iam.service.common;

import com.evo.common.UserAuthority;
import com.evotek.iam.dto.request.*;
import com.evotek.iam.dto.response.TokenResponse;
import com.evotek.iam.dto.response.UserResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface UserService {
    UserResponse getUserInfo(String username);

    UserResponse createUser(UserRequest userRequest);

    UserResponse updateUser(String username,  UpdateUserRequest updateUserRequest);

    void lockUser(String user_id, boolean locked);

    List<UserResponse> search(UserSearchRequest userSearchRequest);

    UserResponse createAdminister(@Valid UserRequest userRequest);

    void changePassword(String username, PasswordRequest passwordRequest);

    TokenResponse processOAuthPostLogin(Authentication authentication);

    int changeAvatar(String username, List<MultipartFile> files);

    List<UserResponse> importUserFile(MultipartFile file);

    UserAuthority getUserAuthority(int userId);

    UserAuthority getUserAuthority(String username);

    UserAuthority getClientAuthority(String clientId);
}
