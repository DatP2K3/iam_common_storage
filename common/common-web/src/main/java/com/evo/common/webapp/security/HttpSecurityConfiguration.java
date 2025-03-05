package com.evo.common.webapp.security;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.evo.common.webapp.config.ActionLogFilter;
import com.evo.common.webapp.config.JwtProperties;
import com.evo.common.webapp.config.RegexPermissionEvaluator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableWebSecurity
@EnableFeignClients(basePackages = {"com.evo.common.client"})
@EnableMethodSecurity
public class HttpSecurityConfiguration {
    private final ActionLogFilter actionLogFilter;
    private final CustomAuthenticationFilter customAuthenticationFilter;
    private final ForbiddenTokenFilter forbiddenTokenFilter;
    private final JwtProperties jwtProperties;
    private final String[] PUBLIC_ENDPOINTS = {
        "/api/users",
        "/api/auth/login_iam",
        "/api/auth/verify-otp",
        "/api/auth/forgot-password",
        "/api/auth/reset-password",
        "/api/certificate/.well-known/jwks.json",
        "/users/authorities/**",
        "/api/public/**",
        "/api/client/authorities/**",
        "/api/client/token/**",
        "/api/uploads/**",
        "/actuator/**",
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/swagger-config",
        "/swagger-ui.html",
        "favicon.ico",
        "/swagger-resources/**",
        "/webjars/**",
        "/swagger-resources/**",
        "/swagger-doc/**",
        "/api/test-retry",
        "api/test/refresh",
        "/api/config/reload"
    };

    public HttpSecurityConfiguration(
            ActionLogFilter actionLogFilter,
            CustomAuthenticationFilter customAuthenticationFilter,
            ForbiddenTokenFilter forbiddenTokenFilter,
            JwtProperties jwtProperties) {
        this.actionLogFilter = actionLogFilter;
        this.customAuthenticationFilter = customAuthenticationFilter;
        this.forbiddenTokenFilter = forbiddenTokenFilter;
        this.jwtProperties = jwtProperties;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(PUBLIC_ENDPOINTS)
                        .permitAll()
                        .requestMatchers("/api/**")
                        .authenticated())
                .oauth2ResourceServer(
                        oauth2 -> oauth2.authenticationManagerResolver(this.jwkResolver(this.jwtProperties)))
                .addFilterAfter(this.forbiddenTokenFilter, BearerTokenAuthenticationFilter.class)
                .addFilterAfter(this.customAuthenticationFilter, BearerTokenAuthenticationFilter.class)
                .addFilterAfter(this.actionLogFilter, BearerTokenAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public AuthenticationManagerResolver<HttpServletRequest> jwkResolver(JwtProperties jwtProperties) {
        return new JwkAuthenticationManagerResolver(jwtProperties);
    }

    @Bean
    MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new RegexPermissionEvaluator());
        return expressionHandler;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
