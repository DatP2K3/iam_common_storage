    package com.evotek.iam.configuration;

    import com.evotek.iam.dto.request.UserRequest;
    import com.evotek.iam.model.Role;
    import com.evotek.iam.repository.RoleRepository;
    import com.evotek.iam.repository.UserRepository;
    import com.evotek.iam.repository.UserRoleRepository;
    import com.evotek.iam.service.common.UserService;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.boot.ApplicationRunner;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.crypto.password.PasswordEncoder;

    @Configuration
    @RequiredArgsConstructor
    @Slf4j
    public class InitConfig {
        private final PasswordEncoder passwordEncoder;
        private final UserService userService;
        private static final String ADMIN_USER_NAME = "bossday";
        private static final String ADMIN_EMAIL = "bossday@gmail.com";
        private static final String ADMIN_PASSWORD = "@123TaoLaAdminNha";

        @Value("${idp.client-id}")
        private String clientId;
        @Value("${idp.client-secret}")
        private String clientSecret;

        @Bean
        ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository, UserRoleRepository userRoleRepository) {
            log.info("Initializing application.....");
            return args -> {
                if (!userRepository.existsByUsername(ADMIN_USER_NAME)) {
                    roleRepository.save(Role.builder()
                        .name("ROLE_USER")
                        .description("Role for user")
                        .isRoot(false)
                        .build());

                    roleRepository.save(Role.builder()
                        .name("ROLE_MANAGER")
                        .description("Role for manager")
                        .isRoot(false)
                        .build());

                    roleRepository.save(Role.builder()
                            .name("ROLE_ADMIN")
                            .description("Role for admin")
                            .isRoot(true)
                            .build());

                    UserRequest userRequest = UserRequest.builder()
                            .username(ADMIN_USER_NAME)
                            .email(ADMIN_EMAIL)
                            .password(ADMIN_PASSWORD)
                            .firstName("Boss")
                            .lastName("Day")
                            .role("ROLE_ADMIN")
                            .build();
                    userService.createAdminister(userRequest);

                    log.warn("Account of admin: BossDay has been created with default password: @123TaoLaAdminNha, please change it");
                }
                log.info("Application initialization completed .....");
            };
        }
    }
