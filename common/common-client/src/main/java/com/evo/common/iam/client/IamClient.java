package com.evo.common.iam.client;

import java.util.UUID;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.evo.common.UserAuthority;
import com.evo.common.dto.response.ApiResponses;
import com.evo.common.iam.config.FeignClientConfiguration;

@FeignClient(
        url = "${app.iam.internal-url:}",
        name = "iam",
        contextId = "common-iam-with-token",
        configuration = FeignClientConfiguration.class,
        fallbackFactory = IamClientFallback.class)
public interface IamClient { // Dùng để lấy thông tin về quyền của user
    @GetMapping("/api/users/authorities/{userId}")
    @LoadBalanced
    ApiResponses<UserAuthority> getUserAuthority(@PathVariable UUID userId);

    @GetMapping("/api/users/authorities-by-username/{username}")
    @LoadBalanced
    ApiResponses<UserAuthority> getUserAuthority(@PathVariable String username);
}
