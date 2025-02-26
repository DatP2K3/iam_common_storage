    package com.evotek.iam.application.configuration;

    import com.evotek.iam.application.dto.request.CreateUserRequest;
    import com.evotek.iam.application.dto.request.CreateUserRoleRequest;
    import com.evotek.iam.application.service.UserCommandService;
    import com.evotek.iam.domain.Role;
    import com.evotek.iam.domain.command.CreateOrUpdateRoleCmd;
    import com.evotek.iam.domain.repository.RoleDomainRepository;
    import com.evotek.iam.domain.repository.UserDomainRepository;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.boot.ApplicationRunner;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;

    import java.util.UUID;

    @Configuration
    @RequiredArgsConstructor
    @Slf4j
    public class InitConfig {
        private final UserCommandService userCommandService;
        private static final String ADMIN_USER_NAME = "bossday";
        private static final String ADMIN_EMAIL = "bossday@gmail.com";
        private static final String ADMIN_PASSWORD = "@123TaoLaAdminNha";

        @Bean
        ApplicationRunner applicationRunner(UserDomainRepository userDomainRepository, RoleDomainRepository roleDomainRepository) {
            log.info("Initializing application.....");
            return args -> {
                if (!userDomainRepository.existsByUsername(ADMIN_USER_NAME)) {
                    CreateOrUpdateRoleCmd createUserRoleCmd = CreateOrUpdateRoleCmd.builder()
                        .name("ROLE_USER")
                        .description("Role for user")
                            .root(false)
                        .build();
                    Role userRole = new Role(createUserRoleCmd);
                    userRole = roleDomainRepository.save(userRole);

                    CreateOrUpdateRoleCmd createManagerRoleCmd = CreateOrUpdateRoleCmd.builder()
                            .name("ROLE_MANAGER")
                            .description("Role for manager")
                            .root(false)
                            .build();
                    Role managerRole = new Role(createManagerRoleCmd);
                    managerRole = roleDomainRepository.save(managerRole);

                    CreateOrUpdateRoleCmd createAdminRoleCmd = CreateOrUpdateRoleCmd.builder()
                            .name("ROLE_ADMIN")
                            .description("Role for admin")
                            .root(true)
                            .build();
                    Role adminRole = new Role(createAdminRoleCmd);
                    adminRole = roleDomainRepository.save(adminRole);

                    UUID roleAdminId = adminRole.getId();
                    CreateUserRoleRequest createUserRoleRequest = CreateUserRoleRequest.builder()
                            .roleId(roleAdminId)
                            .build();

                    CreateUserRequest createUserRequest = CreateUserRequest.builder()
                            .username(ADMIN_USER_NAME)
                            .email(ADMIN_EMAIL)
                            .password(ADMIN_PASSWORD)
                            .firstName("Boss")
                            .lastName("Day")
                            .userRole(createUserRoleRequest)
                            .build();
                    userCommandService.createUser(createUserRequest);

                    log.warn("Account of admin: BossDay has been created with default password: @123TaoLaAdminNha, please change it");
                }
                log.info("Application initialization completed .....");
            };
        }
    }
