package com.evotek.iam.service.common;

import com.evo.common.UserAuthority;
import com.evo.common.dto.response.FileResponse;
import com.evotek.iam.dto.request.*;
import com.evotek.iam.dto.request.identityKeycloak.*;
import com.evotek.iam.dto.response.TokenResponse;
import com.evotek.iam.dto.response.UserResponse;
import com.evotek.iam.exception.AppException;
import com.evotek.iam.exception.ErrorCode;
import com.evotek.iam.exception.ErrorNormalizer;
import com.evotek.iam.mapper.UserMapper;
import com.evotek.iam.model.*;
import com.evotek.iam.repository.*;
import com.evotek.iam.service.self_idp.SelfIDPAuthService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component("self_idp_user_service")
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final IdentityClient identityClient;
    private final ErrorNormalizer errorNormalizer;
    private final EmailService emailService;
    private final SelfIDPAuthService selfIDPAuthService;
    private final FileServiceImpl fileService;
    private final PermissionsRepository permissionsRepository;
    private final OauthClientRepository oauthClientRepository;

    @Value("${idp.client-id}")
    private String clientId;
    @Value("${idp.client-secret}")
    private String clientSecret;
    @Value("${auth.keycloak-enabled}") boolean keycloakEnabled;

    @Override
    public UserResponse getUserInfo(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.userToUserResponse(user);
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        try {
            var token = identityClient.getToken(TokenRequest.builder()
                    .grant_type("client_credentials")
                    .client_id(clientId)
                    .client_secret(clientSecret)
                    .scope("openid")
                    .build());

            var creationResponse = identityClient.createUser(
                    "Bearer " + token.getAccessToken(),
                    UserCreationParamRequestDTO.builder()
                            .username(userRequest.getUsername())
                            .firstName(userRequest.getFirstName())
                            .lastName(userRequest.getLastName())
                            .email(userRequest.getEmail())
                            .enabled(true)
                            .emailVerified(false)

                            .credentials(List.of(CredentialRequestDTO.builder()
                                    .type("password")
                                    .temporary(false)
                                    .value(userRequest.getPassword())
                                    .build()))
                            .build());

            String userId = extractUserId(creationResponse);
            User user = userMapper.UserRequestToUser(userRequest);
            user.setProviderId(userId);
            String password = passwordEncoder.encode(user.getPassword());
            user.setPassword(password);
            if(keycloakEnabled) {
                user.setProvider("keycloak");
            } else {
                user.setProvider("self_idp");
            }
            user = userRepository.save(user);
            Role role = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
            UserRole userRole = UserRole.builder()
                    .userId(user.getSelfUserID())
                    .roleId(role.getId())
                    .build();
            userRoleRepository.save(userRole);
            return userMapper.userToUserResponse(user);
        } catch (FeignException e) {
            throw errorNormalizer.handleKeyCloakException(e);
        }
    }

    @Override
    public UserResponse createAdminister(UserRequest userRequest) {
        try {
            var token = identityClient.getToken(TokenRequest.builder()
                    .grant_type("client_credentials")
                    .client_id(clientId)
                    .client_secret(clientSecret)
                    .scope("openid")
                    .build());

            var creationResponse = identityClient.createUser(
                    "Bearer " + token.getAccessToken(),
                    UserCreationParamRequestDTO.builder()
                            .username(userRequest.getUsername())
                            .firstName(userRequest.getFirstName())
                            .lastName(userRequest.getLastName())
                            .email(userRequest.getEmail())
                            .enabled(true)
                            .emailVerified(false)

                            .credentials(List.of(CredentialRequestDTO.builder()
                                    .type("password")
                                    .temporary(false)
                                    .value(userRequest.getPassword())
                                    .build()))
                            .build());

            String userId = extractUserId(creationResponse);
            User user = userMapper.UserRequestToUser(userRequest);
            user.setProviderId(userId);
            String password = passwordEncoder.encode(user.getPassword());
            user.setPassword(password);
            if(keycloakEnabled) {
                user.setProvider("keycloak");
            } else {
                user.setProvider("self_idp");
            }
            Role role = roleRepository.findByName(userRequest.getRole()).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

            user = userRepository.save(user);
            UserRole userRole = UserRole.builder()
                    .userId(user.getSelfUserID())
                    .roleId(role.getId())
                    .build();
            userRoleRepository.save(userRole);

            return userMapper.userToUserResponse(user);
        } catch (FeignException e) {
            throw errorNormalizer.handleKeyCloakException(e);
        }
    }

    @Override
    public void changePassword(String username, PasswordRequest passwordRequest) {
        try {
            User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

            String userId = user.getProviderId();

            var token = identityClient.getToken(TokenRequest.builder()
                    .grant_type("client_credentials")
                    .client_id(clientId)
                    .client_secret(clientSecret)
                    .scope("openid")
                    .build());
            if(passwordEncoder.matches(passwordRequest.getOldPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
                userRepository.save(user);

                ResetPasswordRequest resetPasswordRequest = ResetPasswordRequest.builder().value(passwordRequest.getNewPassword()).build();
                identityClient.resetPassword("Bearer " + token.getAccessToken(), userId, resetPasswordRequest);

                emailService.sendMailAlert(user.getEmail(), "change_password");
            } else {
                throw new AppException(ErrorCode.INVALID_PASSWORD);
            }
        } catch (FeignException e) {
            throw errorNormalizer.handleKeyCloakException(e);
        }
    }
    public TokenResponse processOAuthPostLogin(Authentication authentication) {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");
        User existUser = userRepository.findByEmail(email).orElse(null);

        if (existUser == null) {
            User newUser = User.builder()
                    .providerId(oauth2User.getAttribute("sub"))
                    .username(email)
                    .email(email)
                    .firstName(oauth2User.getAttribute("family_name"))
                    .lastName(oauth2User.getAttribute("name"))
                    .locked(false)
                    .provider("google")
                    .build();
            userRepository.save(newUser);
            Role role = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
            UserRole userRole = UserRole.builder()
                    .userId(newUser.getSelfUserID())
                    .roleId(role.getId())
                    .build();
            userRoleRepository.save(userRole);
            var accessToken = selfIDPAuthService.generateToken(newUser, false, false);
            var refreshToken = selfIDPAuthService.generateToken(newUser, false, true);
            System.out.println("Created new user: " + email);
            return TokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
        }
            var accessToken = selfIDPAuthService.generateToken(existUser, false, false);
            var refreshToken = selfIDPAuthService.generateToken(existUser, false, true);
            return TokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    @Override
    public int changeAvatar(String username, List<MultipartFile> files) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        FileResponse fileResponse = fileService.uploadFile(files, true, "avatar").getFirst();
        int avatar = fileResponse.getId();
        user.setAvatarFileId(avatar);
        userRepository.save(user);
        return avatar;
    }

    @Override
    public List<UserResponse> importUserFile(MultipartFile file) {
        try {
            List<UserResponse> userResponses = new ArrayList<>();
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();
            rowIterator.next();

            while (rowIterator.hasNext()) {
                List<String> errors = new ArrayList<>();
                Row row = rowIterator.next();
                User user = new User();
                int rowIndex = row.getRowNum() + 1;
                Cell usernameCell = row.getCell(0);
                Cell passwordCell = row.getCell(1);
                Cell emailNameCell = row.getCell(2);
                Cell firstNameCell = row.getCell(3);
                Cell lastNameCell = row.getCell(4);
                Cell dobCell = row.getCell(5);
                Cell streetCell = row.getCell(6);
                Cell wardCell = row.getCell(7);
                Cell districtCell = row.getCell(8);
                Cell cityCell = row.getCell(9);
                Cell yearsOfExperienceCell = row.getCell(10);

                if (usernameCell != null) {
                    String username = usernameCell.getStringCellValue().trim();
                    if (username.isEmpty()) {
                        errors.add("Dòng " + rowIndex + ": Username bị trống.");
                    } else if (userRepository.existsByUsername(username)) {
                        errors.add("Dòng " + rowIndex + ": Username '" + username + "' đã tồn tại.");
                    } else {
                        user.setUsername(username);
                    }
                } else {
                    errors.add("Dòng " + rowIndex + ": Username bị trống.");
                }

                if (passwordCell != null) {
                    String password = passwordCell.getStringCellValue().trim();
                    if (password.isEmpty()) {
                        errors.add("Dòng " + rowIndex + ": Password bị trống.");
                    } else {
                        user.setPassword(passwordEncoder.encode(password));
                    }
                } else {
                    errors.add("Dòng " + rowIndex + ": Password bị trống.");
                }

                if (emailNameCell != null) {
                    String email = emailNameCell.getStringCellValue().trim();
                    if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) { // Regex kiểm tra định dạng email
                        errors.add("Dòng " + rowIndex + ": Email không hợp lệ.");
                    } else {
                        user.setEmail(email);
                    }
                }

                if (firstNameCell != null && lastNameCell != null) {
                    String firstName = firstNameCell.getStringCellValue().trim();
                    String lastName = lastNameCell.getStringCellValue().trim();
                    if (firstName.isEmpty() || lastName.isEmpty()) {
                        errors.add("Dòng " + rowIndex + ": Họ hoặc Tên bị trống.");
                    } else {
                        user.setFirstName(firstName);
                        user.setLastName(lastName);
                    }
                } else {
                    errors.add("Dòng " + rowIndex + ": Họ hoặc Tên bị trống.");
                }

                if (dobCell != null) {
                    try {
                        user.setDob(dobCell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    } catch (Exception e) {
                        errors.add("Dòng " + rowIndex + ": Ngày sinh không hợp lệ.");
                    }
                } else {
                    errors.add("Dòng " + rowIndex + ": Ngày sinh bị trống.");
                }

                if (yearsOfExperienceCell != null) {
                    try {
                        user.setYearsOfExperience((int) yearsOfExperienceCell.getNumericCellValue());
                    } catch (Exception e) {
                        errors.add("Dòng " + rowIndex + ": Số năm kinh nghiệm phải là số.");
                    }
                } else {
                    errors.add("Dòng " + rowIndex + ": Số năm kinh nghiệm bị trống.");
                }

                if (streetCell != null) user.setStreet(streetCell.getStringCellValue().trim());
                if (wardCell != null) user.setWard(wardCell.getStringCellValue().trim());
                if (districtCell != null) user.setDistrict(districtCell.getStringCellValue().trim());
                if (cityCell != null) user.setCity(cityCell.getStringCellValue().trim());

                if (!errors.isEmpty()) {
                    for (String error : errors) {
                        log.warn(error);
                    }
                } else {
                    userResponses.add(userMapper.userToUserResponse(userRepository.save(user)));
                }
            }
            return userResponses;
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_NOT_FOUND);
        }
    }

    private String extractUserId(ResponseEntity<?> response) {
        String location = response.getHeaders().get("Location").getFirst();
        String[] splitedStr = location.split("/");
        return splitedStr[splitedStr.length - 1];
    }

    @Override
    public UserResponse updateUser(String username, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (updateUserRequest.getEmail() != null) {
            user.setEmail(updateUserRequest.getEmail());
        }
        if (updateUserRequest.getFirstName() != null) {
            user.setFirstName(updateUserRequest.getFirstName());
        }
        if (updateUserRequest.getLastName() != null) {
            user.setLastName(updateUserRequest.getLastName());
        }
        if (updateUserRequest.getDob() != null) {
            user.setDob(updateUserRequest.getDob());
        }
        if (updateUserRequest.getStreet() != null) {
            user.setStreet(updateUserRequest.getStreet());
        }
        if (updateUserRequest.getWard() != null) {
            user.setWard(updateUserRequest.getWard());
        }
        if (updateUserRequest.getDistrict() != null) {
            user.setDistrict(updateUserRequest.getDistrict());
        }
        if (updateUserRequest.getCity() != null) {
            user.setCity(updateUserRequest.getCity());
        }
        User user1 = userRepository.save(user);
        return userMapper.userToUserResponse(user1);
    }

    @Override
    public void lockUser(String username, boolean enabled) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setLocked(!enabled);
        userRepository.save(user);

        var token = identityClient.getToken(TokenRequest.builder()
                .grant_type("client_credentials")
                .client_id(clientId)
                .client_secret(clientSecret)
                .scope("openid")
                .build());

        identityClient.lockUser("Bearer " + token.getAccessToken(), user.getProviderId(), LockUserRequest.builder().enabled(enabled).build());
    }

    @Override
    public List<UserResponse> search(UserSearchRequest userSearchRequest) {
        List<User> users = userRepository.search(userSearchRequest);
        List<UserResponse> userResponses = users.stream().map(userMapper::userToUserResponse).toList();
        return userResponses;
    }

    @Override
    public UserAuthority getUserAuthority(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        UserRole userRole = userRoleRepository.findByUserId(user.getSelfUserID()).orElseThrow(() -> new RuntimeException("User role not found"));
        Role role = roleRepository.findById(userRole.getRoleId()).orElseThrow(() -> new RuntimeException("Role not found"));
        boolean isRoot = role.isRoot();
        List<Permission> permissions = permissionsRepository.findPermissionByRoleId(role.getId());
        List<String> grantedPermissions = permissions.stream()
                .map(permission -> permission.getResourceId() + "." + permission.getScope())
                .toList();
        return UserAuthority.builder().userId(userId).isRoot(isRoot).isClient(false).grantedPermissions(grantedPermissions).build();
    }

    @Override
    public UserAuthority getUserAuthority(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        UserRole userRole = userRoleRepository.findByUserId(user.getSelfUserID()).orElseThrow(() -> new RuntimeException("User role not found"));
        Role role = roleRepository.findById(userRole.getRoleId()).orElseThrow(() -> new RuntimeException("Role not found"));
        boolean isRoot = role.isRoot();
        List<Permission> permissions = permissionsRepository.findPermissionByRoleId(role.getId());
        List<String> grantedPermissions = permissions.stream()
                .map(permission -> permission.getResourceId() + "." + permission.getScope())
                .toList();
        return UserAuthority.builder().userId(user.getSelfUserID()).isRoot(isRoot).isClient(false).grantedPermissions(grantedPermissions).build();
    }

    @Override
    public UserAuthority getClientAuthority(String clientId) {
        OauthClient oauthClient = oauthClientRepository.findByClientId(clientId).orElseThrow(() -> new AppException(ErrorCode.CLIENT_NOT_EXISTED));
        return UserAuthority.builder().userId(oauthClient.getId()).isRoot(false).isClient(true).build();
    }
}
