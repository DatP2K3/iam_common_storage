package com.evo.common.webapp.security;

import com.evo.common.UserAuthentication;
import com.evo.common.UserAuthority;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends OncePerRequestFilter {
    private final AuthorityService authorityService;

    @Value("${oauth.client.iam.client-id}")
    private String clientId;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        log.info("CustomAuthenticationFilter");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        JwtAuthenticationToken authentication =
                (JwtAuthenticationToken) securityContext.getAuthentication();
        Jwt token = authentication.getToken();

        Boolean isRoot = Boolean.FALSE;
        Boolean isClient = Boolean.FALSE;

        Optional<UserAuthority> optionalUserAuthority = enrichAuthority(token);
        Set<SimpleGrantedAuthority> grantedPermissions = new HashSet<>();

        UserAuthority userAuthority;
        if (optionalUserAuthority.isPresent()) {
            userAuthority = optionalUserAuthority.get();
            isRoot = userAuthority.getIsRoot();
            isClient = userAuthority.getIsClient();

            if (userAuthority.getGrantedPermissions() != null) {
                for (String permission : userAuthority.getGrantedPermissions()) {
                    grantedPermissions.add(new SimpleGrantedAuthority(permission));
                }
            }
        }

        String username;
        if (StringUtils.hasText(token.getClaimAsString("preferred_username"))) {
            username = token.getClaimAsString("preferred_username");
        } else {
            username = token.getClaimAsString("sub");
        }

        User principal = new User(username, "", grantedPermissions);
        AbstractAuthenticationToken auth =
                new UserAuthentication(principal, token, grantedPermissions, isRoot, isClient);

        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {  // Quyết định xem bộ lọc có nên được áp dụng cho yêu cầu hay không. Trong trường hợp này, nó chỉ áp dụng bộ lọc khi yêu cầu có chứa một JwtAuthenticationToken, tức là yêu cầu có chứa JWT hợp lệ.
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        return !(authentication instanceof JwtAuthenticationToken);
    }

    private Optional<UserAuthority> enrichAuthority(Jwt token) { // Lấy thông tin về quyền của user từ token
        log.warn(token.toString());
        if (StringUtils.hasText(token.getClaimAsString("userId")) && token.getClaimAsString("userId").equals("common")) {
            return Optional.ofNullable(authorityService.getClientAuthority(token.getClaimAsString("sub")));
        }
        String username;
        if (StringUtils.hasText(token.getClaimAsString("preferred_username"))) {
            username = token.getClaimAsString("preferred_username");
        } else {
            username = token.getClaimAsString("sub");
        }
        return Optional.ofNullable(authorityService.getUserAuthority(username));
    }
}
