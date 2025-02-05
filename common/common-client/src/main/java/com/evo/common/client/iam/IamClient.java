package com.evo.common.client.iam;

import com.evo.common.UserAuthority;
import com.evo.common.config.FeignClientConfiguration;
import com.evo.common.dto.response.ApiResponses;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        url = "${app.iam.internal-url:}",
        name = "iam",
        contextId = "common-iam-with-token",
        configuration = FeignClientConfiguration.class,
        fallbackFactory = IamClientFallback.class)
public interface IamClient { // Dùng để lấy thông tin về quyền của user
    @GetMapping("/api/users/authorities/{userId}")
    @LoadBalanced
    ApiResponses<UserAuthority> getUserAuthority(@PathVariable int userId);

    @GetMapping("/api/users/authorities-by-username/{username}")
    @LoadBalanced
    ApiResponses<UserAuthority> getUserAuthority(@PathVariable String username);
}