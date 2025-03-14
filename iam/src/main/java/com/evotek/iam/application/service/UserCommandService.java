package com.evotek.iam.application.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.evo.common.dto.event.PushNotificationEvent;
import com.evotek.iam.application.dto.request.ChangePasswordRequest;
import com.evotek.iam.application.dto.request.CreateUserRequest;
import com.evotek.iam.application.dto.request.UpdateUserRequest;
import com.evotek.iam.application.dto.response.UserDTO;

@Service
public interface UserCommandService {
    UserDTO createDefaultUser(CreateUserRequest request);

    UserDTO createUser(CreateUserRequest request);

    void changePassword(String username, ChangePasswordRequest request);

    UUID changeAvatar(String username, List<MultipartFile> files);

    List<UserDTO> importUserFile(MultipartFile file);

    UserDTO updateUser(String username, UpdateUserRequest updateUserRequest);

    void lockUser(String username, boolean enabled);

    void testFcm(PushNotificationEvent pushNotificationEvent);
}
