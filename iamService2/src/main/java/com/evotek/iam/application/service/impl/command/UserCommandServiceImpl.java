package com.evotek.iam.application.service.impl.command;

import com.evo.common.dto.response.FileResponse;
import com.evotek.iam.application.dto.mapper.UserDTOMapper;
import com.evotek.iam.application.dto.request.ChangePasswordRequest;
import com.evotek.iam.application.dto.request.CreateUserRequest;
import com.evotek.iam.application.dto.request.UpdateUserRequest;
import com.evotek.iam.domain.UserRole;
import com.evotek.iam.domain.command.*;
import com.evotek.iam.application.dto.response.UserDTO;
import com.evotek.iam.application.mapper.CommandMapper;
import com.evotek.iam.infrastructure.adapter.storage.FileService;
import com.evotek.iam.infrastructure.adapter.email.EmailService;
import com.evotek.iam.infrastructure.adapter.keycloak.KeycloakCommandClient;
import com.evotek.iam.infrastructure.adapter.keycloak.KeycloakQueryClient;
import com.evotek.iam.application.service.UserCommandService;
import com.evotek.iam.domain.Role;
import com.evotek.iam.domain.User;
import com.evotek.iam.domain.repository.RoleDomainRepository;
import com.evotek.iam.domain.repository.UserDomainRepository;
import com.evotek.iam.infrastructure.adapter.keycloak.KeycloakIdentityClient;
import com.evotek.iam.infrastructure.support.exception.*;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCommandServiceImpl implements UserCommandService {
    private final KeycloakCommandClient keycloakCommandClient;
    private final CommandMapper commandMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserDomainRepository userDomainRepository;
    private final UserDTOMapper userDTOMapper;
    private final RoleDomainRepository roleDomainRepository;
    private final ErrorNormalizer errorNormalizer;
    private final KeycloakQueryClient keycloakQueryClient;
    private final KeycloakIdentityClient keycloakIdentityClient;
    private final EmailService emailService;
    private final FileService fileService;
    @Value("${auth.keycloak-enabled}") boolean keycloakEnabled;

    @Override
    public UserDTO createDefaultUser(CreateUserRequest request) {
        try {
            CreateUserCmd createUserCmd = commandMapper.from(request);
            createUserCmd.setPassword(passwordEncoder.encode(createUserCmd.getPassword()));

            if(keycloakEnabled) {
                createUserCmd.setProvider("keycloak");
            } else {
                createUserCmd.setProvider("self_idp");
            }
            createUserCmd.setProviderId(UUID.fromString(keycloakCommandClient.createKeycloakUser(request)));
            Role role = roleDomainRepository.findByName("ROLE_USER");
            User user = new User(createUserCmd);
            CreateUserRoleCmd createUserRoleCmd = new CreateUserRoleCmd(role.getId());
            UserRole userRole = new UserRole(createUserRoleCmd, user.getSelfUserID());
            user.setUserRole(userRole);
            user = userDomainRepository.save(user);
            return userDTOMapper.domainModelToDTO(user);
        } catch (FeignException e) {
            throw errorNormalizer.handleKeyCloakException(e);
        }
    }

    @Override
    public UserDTO createUser(CreateUserRequest request) {
        UUID keycloakUserId = UUID.fromString(keycloakCommandClient.createKeycloakUser(request));

        CreateUserCmd createUserCmd = commandMapper.from(request);
        createUserCmd.setPassword(passwordEncoder.encode(createUserCmd.getPassword()));

        if(keycloakEnabled) {
            createUserCmd.setProvider("keycloak");
        } else {
            createUserCmd.setProvider("self_idp");
        }
        createUserCmd.setProviderId(keycloakUserId);
        User user = new User(createUserCmd);
        userDomainRepository.save(user);
        return userDTOMapper.domainModelToDTO(user);
    }

    @Override
    public void changePassword(String username, ChangePasswordRequest request) {
        ChangePasswordCmd changePasswordCmd = commandMapper.from(request);

        User user = userDomainRepository.getByUsername(username);
        UUID keycloakUserId = user.getProviderId();

        String token = keycloakQueryClient.getClientToken();
        if(passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            ResetKeycloakPasswordCmd resetKeycloakPasswordCmd = ResetKeycloakPasswordCmd.builder().value(changePasswordCmd.getNewPassword()).build();
            keycloakCommandClient.resetPassword("Bearer " + token, keycloakUserId, resetKeycloakPasswordCmd);

            user.changePassword(passwordEncoder.encode(request.getNewPassword()));
            userDomainRepository.save(user);

            emailService.sendMailAlert(user.getEmail(), "change_password");
        } else {
            throw new AuthException(AuthErrorCode.INVALID_PASSWORD);
        }
    }

    @Override
    public UUID changeAvatar(String username, List<MultipartFile> files) {
        User user = userDomainRepository.getByUsername(username);
        FileResponse fileResponse = fileService.uploadFile(files, true, "avatar").getFirst();
        UUID avatarId = fileResponse.getId();
        user.changeAvatar(avatarId);
        userDomainRepository.save(user);
        return avatarId;
    }

    @Override
    public List<UserDTO> importUserFile(MultipartFile file) {
        try {
            List<UserDTO> userDTOS = new ArrayList<>();
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();
            rowIterator.next();

            while (rowIterator.hasNext()) {
                List<String> errors = new ArrayList<>();
                Row row = rowIterator.next();

                CreateUserRequest.CreateUserRequestBuilder createUserRequestBuilder = CreateUserRequest.builder();

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
                    } else if (userDomainRepository.existsByUsername(username)) {
                        errors.add("Dòng " + rowIndex + ": Username '" + username + "' đã tồn tại.");
                    } else {
                        createUserRequestBuilder.username(username);
                    }
                } else {
                    errors.add("Dòng " + rowIndex + ": Username bị trống.");
                }

                if (passwordCell != null) {
                    String password = passwordCell.getStringCellValue().trim();
                    if (password.isEmpty()) {
                        errors.add("Dòng " + rowIndex + ": Password bị trống.");
                    } else {
                        createUserRequestBuilder.password(passwordEncoder.encode(password));
                    }
                } else {
                    errors.add("Dòng " + rowIndex + ": Password bị trống.");
                }

                if (emailNameCell != null) {
                    String email = emailNameCell.getStringCellValue().trim();
                    if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                        errors.add("Dòng " + rowIndex + ": Email không hợp lệ.");
                    } else {
                        createUserRequestBuilder.email(email);
                    }
                }

                if (firstNameCell != null && lastNameCell != null) {
                    String firstName = firstNameCell.getStringCellValue().trim();
                    String lastName = lastNameCell.getStringCellValue().trim();
                    if (firstName.isEmpty() || lastName.isEmpty()) {
                        errors.add("Dòng " + rowIndex + ": Họ hoặc Tên bị trống.");
                    } else {
                        createUserRequestBuilder.firstName(firstName);
                        createUserRequestBuilder.lastName(lastName);
                    }
                } else {
                    errors.add("Dòng " + rowIndex + ": Họ hoặc Tên bị trống.");
                }

                if (dobCell != null) {
                    try {
                        createUserRequestBuilder.dob(dobCell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    } catch (Exception e) {
                        errors.add("Dòng " + rowIndex + ": Ngày sinh không hợp lệ.");
                    }
                } else {
                    errors.add("Dòng " + rowIndex + ": Ngày sinh bị trống.");
                }

                if (yearsOfExperienceCell != null) {
                    try {
                        createUserRequestBuilder.yearsOfExperience((int) yearsOfExperienceCell.getNumericCellValue());
                    } catch (Exception e) {
                        errors.add("Dòng " + rowIndex + ": Số năm kinh nghiệm phải là số.");
                    }
                } else {
                    errors.add("Dòng " + rowIndex + ": Số năm kinh nghiệm bị trống.");
                }

                if (streetCell != null) createUserRequestBuilder.street(streetCell.getStringCellValue().trim());
                if (wardCell != null) createUserRequestBuilder.ward(wardCell.getStringCellValue().trim());
                if (districtCell != null) createUserRequestBuilder.district(districtCell.getStringCellValue().trim());
                if (cityCell != null) createUserRequestBuilder.city(cityCell.getStringCellValue().trim());

                if (!errors.isEmpty()) {
                    for (String error : errors) {
                        log.warn(error);
                    }
                } else {
                    CreateUserRequest request = createUserRequestBuilder.build();
                    userDTOS.add(createDefaultUser(request));
                }
            }
            return userDTOS;
        } catch (IOException e) {
            throw new AppException(AppErrorCode.FILE_NOT_FOUND);
        }
    }

    @Override
    public UserDTO updateUser(String username, UpdateUserRequest updateUserRequest) {
        UpdateUserCmd cmd = commandMapper.from(updateUserRequest);
        User user = userDomainRepository.getByUsername(username);
        user.update(cmd);
        return userDTOMapper.domainModelToDTO(userDomainRepository.save(user));
    }

    @Override
    public void lockUser(String username, boolean enabled) {
        User user = userDomainRepository.getByUsername(username);
        user.setLocked(!enabled);
        userDomainRepository.save(user);
        String token = keycloakQueryClient.getClientToken();
        keycloakIdentityClient.lockUser("Bearer " + token, user.getProviderId().toString(), LockUserCmd.builder().enabled(enabled).build());
    }

}
