package com.evotek.iam.application.service.impl.command;

import java.io.IOException;
import java.time.ZoneId;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.evo.common.dto.request.PushNotificationRequest;
import com.evo.common.dto.request.SendNotificationRequest;
import com.evo.common.dto.response.FileResponse;
import com.evo.common.enums.Channel;
import com.evo.common.enums.KafkaTopic;
import com.evo.common.enums.TemplateCode;
import com.evo.common.support.SecurityContextUtils;
import com.evotek.iam.application.dto.mapper.UserDTOMapper;
import com.evotek.iam.application.dto.request.ChangePasswordRequest;
import com.evotek.iam.application.dto.request.CreateUserRequest;
import com.evotek.iam.application.dto.request.UpdateUserRequest;
import com.evotek.iam.application.dto.response.UserDTO;
import com.evotek.iam.application.mapper.CommandMapper;
import com.evotek.iam.application.service.UserCommandService;
import com.evotek.iam.domain.Role;
import com.evotek.iam.domain.User;
import com.evotek.iam.domain.UserActivityLog;
import com.evotek.iam.domain.UserRole;
import com.evotek.iam.domain.command.*;
import com.evotek.iam.domain.repository.RoleDomainRepository;
import com.evotek.iam.domain.repository.UserDomainRepository;
import com.evotek.iam.infrastructure.adapter.keycloak.KeycloakService;
import com.evotek.iam.infrastructure.adapter.notification.NotificationService;
import com.evotek.iam.infrastructure.adapter.storage.FileService;
import com.evotek.iam.infrastructure.support.exception.*;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCommandServiceImpl implements UserCommandService {
    private final KeycloakService keycloakService;
    private final CommandMapper commandMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserDomainRepository userDomainRepository;
    private final UserDTOMapper userDTOMapper;
    private final RoleDomainRepository roleDomainRepository;
    private final ErrorNormalizer errorNormalizer;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final FileService fileService;
    private final NotificationService notificationService;

    @Value("${auth.keycloak-enabled}")
    boolean keycloakEnabled;

    @Override
    public UserDTO createDefaultUser(CreateUserRequest request) {
        try {
            CreateUserCmd createUserCmd = commandMapper.from(request);
            createUserCmd.setPassword(passwordEncoder.encode(createUserCmd.getPassword()));

            if (keycloakEnabled) {
                createUserCmd.setProvider("keycloak");
            } else {
                createUserCmd.setProvider("self_idp");
            }
            createUserCmd.setProviderId(UUID.fromString(keycloakService.createKeycloakUser(request)));
            Role role = roleDomainRepository.findByName("ROLE_USER");
            User user = new User(createUserCmd);
            CreateUserRoleCmd createUserRoleCmd = new CreateUserRoleCmd(role.getId());
            UserRole userRole = new UserRole(createUserRoleCmd, user.getSelfUserID());
            user.setUserRole(userRole);
            user = userDomainRepository.save(user);

            notificationService.initUserTopic(user.getSelfUserID());

            Map<String, Object> params = SecurityContextUtils.getSecurityContextMap();
            params.put("username", user.getUsername());
            params.put("email", user.getEmail());
            SendNotificationRequest mailAlert = SendNotificationRequest.builder()
                    .channel(Channel.EMAIL.name())
                    .recipient(user.getEmail())
                    .templateCode(TemplateCode.OTP_ALERT)
                    .param(params)
                    .build();
            kafkaTemplate.send(KafkaTopic.SEND_NOTIFICATION_GROUP.getTopicName(), mailAlert);

            return userDTOMapper.domainModelToDTO(user);
        } catch (FeignException e) {
            throw errorNormalizer.handleKeyCloakException(e);
        }
    }

    @Override
    public UserDTO createUser(CreateUserRequest request) {
        UUID keycloakUserId = UUID.fromString(keycloakService.createKeycloakUser(request));

        CreateUserCmd createUserCmd = commandMapper.from(request);
        createUserCmd.setPassword(passwordEncoder.encode(createUserCmd.getPassword()));

        if (keycloakEnabled) {
            createUserCmd.setProvider("keycloak");
        } else {
            createUserCmd.setProvider("self_idp");
        }
        createUserCmd.setProviderId(keycloakUserId);
        User user = new User(createUserCmd);
        userDomainRepository.save(user);

        Map<String, Object> params = SecurityContextUtils.getSecurityContextMap();
        params.put("username", user.getUsername());
        params.put("email", user.getEmail());
        SendNotificationRequest mailAlert = SendNotificationRequest.builder()
                .channel(Channel.EMAIL.name())
                .recipient(user.getEmail())
                .templateCode(TemplateCode.SIGNIN_ALERT)
                .param(params)
                .build();
        kafkaTemplate.send(KafkaTopic.SEND_NOTIFICATION_GROUP.getTopicName(), mailAlert);

        return userDTOMapper.domainModelToDTO(user);
    }

    @Override
    public void changePassword(String username, ChangePasswordRequest request) {
        ChangePasswordCmd changePasswordCmd = commandMapper.from(request);

        User user = userDomainRepository.getByUsername(username);
        UUID keycloakUserId = user.getProviderId();

        if (passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            ResetKeycloakPasswordCmd resetKeycloakPasswordCmd = ResetKeycloakPasswordCmd.builder()
                    .value(changePasswordCmd.getNewPassword())
                    .build();
            keycloakService.resetPassword(keycloakUserId, resetKeycloakPasswordCmd);

            user.changePassword(passwordEncoder.encode(request.getNewPassword()));
            WriteLogCmd logCmd = commandMapper.from("Change Password");
            UserActivityLog userActivityLog = new UserActivityLog(logCmd);
            user.setUserActivityLog(userActivityLog);
            userDomainRepository.save(user);

            Map<String, Object> params = SecurityContextUtils.getSecurityContextMap();
            SendNotificationRequest mailAlert = SendNotificationRequest.builder()
                    .channel(Channel.EMAIL.name())
                    .recipient(user.getEmail())
                    .templateCode(TemplateCode.PASSWORD_CHANGE_ALERT)
                    .param(params)
                    .build();
            kafkaTemplate.send(KafkaTopic.SEND_NOTIFICATION_GROUP.getTopicName(), mailAlert);

        } else {
            throw new AuthException(AuthErrorCode.INVALID_PASSWORD);
        }
    }

    @Override
    public UUID changeAvatar(String username, List<MultipartFile> files) {
        User user = userDomainRepository.getByUsername(username);
        FileResponse fileResponse =
                fileService.uploadFile(files, true, "avatar").getFirst();
        UUID avatarId = fileResponse.getId();
        user.changeAvatar(avatarId);

        WriteLogCmd logCmd = commandMapper.from("Change Avatar");
        UserActivityLog userActivityLog = new UserActivityLog(logCmd);
        user.setUserActivityLog(userActivityLog);
        userDomainRepository.save(user);

        return avatarId;
    }

    @Override
    public List<UserDTO> importUserFile(MultipartFile file) {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next(); // Skip header row
            rowIterator.next(); // Skip another row if needed

            List<UserDTO> userDTOS = new ArrayList<>();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                CreateUserRequest.CreateUserRequestBuilder createUserRequestBuilder = CreateUserRequest.builder();
                List<String> errors = new ArrayList<>();
                int rowIndex = row.getRowNum() + 1;

                processUsernameCell(row.getCell(0), rowIndex, createUserRequestBuilder, errors);
                processPasswordCell(row.getCell(1), rowIndex, createUserRequestBuilder, errors);
                processEmailCell(row.getCell(2), rowIndex, createUserRequestBuilder, errors);
                processNameCells(row.getCell(3), row.getCell(4), rowIndex, createUserRequestBuilder, errors);
                processDobCell(row.getCell(5), rowIndex, createUserRequestBuilder, errors);
                processYearsOfExperienceCell(row.getCell(10), rowIndex, createUserRequestBuilder, errors);
                processAddressCells(row, createUserRequestBuilder);

                if (!errors.isEmpty()) {
                    logErrors(errors);
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

    private void processUsernameCell(
            Cell cell, int rowIndex, CreateUserRequest.CreateUserRequestBuilder builder, List<String> errors) {
        if (cell != null) {
            String username = cell.getStringCellValue().trim();
            if (username.isEmpty()) {
                errors.add("Dòng " + rowIndex + ": Username bị trống.");
            } else if (userDomainRepository.existsByUsername(username)) {
                errors.add("Dòng " + rowIndex + ": Username '" + username + "' đã tồn tại.");
            } else {
                builder.username(username);
            }
        } else {
            errors.add("Dòng " + rowIndex + ": Username bị trống.");
        }
    }

    private void processPasswordCell(
            Cell cell, int rowIndex, CreateUserRequest.CreateUserRequestBuilder builder, List<String> errors) {
        if (cell != null) {
            String password = cell.getStringCellValue().trim();
            if (password.isEmpty()) {
                errors.add("Dòng " + rowIndex + ": Password bị trống.");
            } else {
                builder.password(passwordEncoder.encode(password));
            }
        } else {
            errors.add("Dòng " + rowIndex + ": Password bị trống.");
        }
    }

    private void processEmailCell(
            Cell cell, int rowIndex, CreateUserRequest.CreateUserRequestBuilder builder, List<String> errors) {
        if (cell != null) {
            String email = cell.getStringCellValue().trim();
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                errors.add("Dòng " + rowIndex + ": Email không hợp lệ.");
            } else {
                builder.email(email);
            }
        }
    }

    private void processNameCells(
            Cell firstNameCell,
            Cell lastNameCell,
            int rowIndex,
            CreateUserRequest.CreateUserRequestBuilder builder,
            List<String> errors) {
        if (firstNameCell != null && lastNameCell != null) {
            String firstName = firstNameCell.getStringCellValue().trim();
            String lastName = lastNameCell.getStringCellValue().trim();
            if (firstName.isEmpty() || lastName.isEmpty()) {
                errors.add("Dòng " + rowIndex + ": Họ hoặc Tên bị trống.");
            } else {
                builder.firstName(firstName);
                builder.lastName(lastName);
            }
        } else {
            errors.add("Dòng " + rowIndex + ": Họ hoặc Tên bị trống.");
        }
    }

    private void processDobCell(
            Cell cell, int rowIndex, CreateUserRequest.CreateUserRequestBuilder builder, List<String> errors) {
        if (cell != null) {
            try {
                builder.dob(cell.getDateCellValue()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate());
            } catch (Exception e) {
                errors.add("Dòng " + rowIndex + ": Ngày sinh không hợp lệ.");
            }
        } else {
            errors.add("Dòng " + rowIndex + ": Ngày sinh bị trống.");
        }
    }

    private void processYearsOfExperienceCell(
            Cell cell, int rowIndex, CreateUserRequest.CreateUserRequestBuilder builder, List<String> errors) {
        if (cell != null) {
            try {
                builder.yearsOfExperience((int) cell.getNumericCellValue());
            } catch (Exception e) {
                errors.add("Dòng " + rowIndex + ": Số năm kinh nghiệm phải là số.");
            }
        } else {
            errors.add("Dòng " + rowIndex + ": Số năm kinh nghiệm bị trống.");
        }
    }

    private void processAddressCells(Row row, CreateUserRequest.CreateUserRequestBuilder builder) {
        if (row.getCell(6) != null)
            builder.street(row.getCell(6).getStringCellValue().trim());
        if (row.getCell(7) != null)
            builder.ward(row.getCell(7).getStringCellValue().trim());
        if (row.getCell(8) != null)
            builder.district(row.getCell(8).getStringCellValue().trim());
        if (row.getCell(9) != null)
            builder.city(row.getCell(9).getStringCellValue().trim());
    }

    private void logErrors(List<String> errors) {
        for (String error : errors) {
            log.warn(error);
        }
    }

    @Override
    public UserDTO updateUser(String username, UpdateUserRequest updateUserRequest) {
        UpdateUserCmd cmd = commandMapper.from(updateUserRequest);
        User user = userDomainRepository.getByUsername(username);
        user.update(cmd);

        WriteLogCmd logCmd = commandMapper.from("Change Avatar");
        UserActivityLog userActivityLog = new UserActivityLog(logCmd);
        user.setUserActivityLog(userActivityLog);
        return userDTOMapper.domainModelToDTO(userDomainRepository.save(user));
    }

    @Override
    public void lockUser(String username, boolean enabled) {
        try {
            User user = userDomainRepository.getByUsername(username);
            user.setLocked(!enabled);
            userDomainRepository.save(user);
            keycloakService.lockUser(user.getProviderId(), enabled);
            Map<String, Object> params = SecurityContextUtils.getSecurityContextMap();
            params.put("username", username);
            SendNotificationRequest mailAlert = SendNotificationRequest.builder()
                    .channel(Channel.EMAIL.name())
                    .recipient(user.getEmail())
                    .templateCode(TemplateCode.LOCK_USER_ALERT)
                    .param(params)
                    .build();
            kafkaTemplate.send("notification-group", mailAlert);
        } catch (FeignException e) {
            throw errorNormalizer.handleKeyCloakException(e);
        }
    }

    @Override
    public void testFcm(PushNotificationRequest pushNotificationRequest) {
        kafkaTemplate.send(KafkaTopic.PUSH_NOTIFICATION_GROUP.getTopicName(), pushNotificationRequest);
    }
}
