package com.evo.common.iam.client;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.evo.common.UserAuthority;
import com.evo.common.dto.response.ApiResponses;
import com.evo.common.iam.config.FeignClientNoTokenConfiguration;

@FeignClient(
        url = "${app.iam.internal-url:}",
        name = "iam-no-token",
        contextId = "common-iam-no-token",
        configuration = FeignClientNoTokenConfiguration.class,
        fallbackFactory = IamNoTokenClientFallback.class)
public interface IamNoTokenClient {
    @GetMapping("/api/client/authorities/{clientId}")
    @LoadBalanced
    ApiResponses<UserAuthority> getClientAuthority(@PathVariable String clientId);
}
