package com.evotek.iam.infrastructure.adapter.google;

import com.evotek.iam.application.dto.response.OutboundUserDTO;
import com.evotek.iam.infrastructure.adapter.google.config.GoogleClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "google-us-client",
        url = "https://www.googleapis.com",
        contextId = "google-user-client",
        configuration = GoogleClientConfiguration.class)
public interface GoogleUserClient {
    @GetMapping(value = "/oauth2/v1/userinfo")
    OutboundUserDTO getUserInfo(@RequestParam("alt") String alt, @RequestParam("access_token") String accessToken);
}
