package com.evotek.iam.infrastructure.adapter.google;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.evotek.iam.application.dto.request.ExchangeTokenRequest;
import com.evotek.iam.application.dto.response.OutboundUserDTO;
import com.evotek.iam.application.dto.response.TokenDTO;
import com.evotek.iam.infrastructure.adapter.google.config.GoogleClientConfiguration;

import feign.QueryMap;

@FeignClient(
        name = "google-identity-client",
        url = "https://oauth2.googleapis.com",
        contextId = "google-identity-client",
        configuration = GoogleClientConfiguration.class)
public interface GoogleIdentityClient {
    @PostMapping(value = "/token", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenDTO exchangeToken(@QueryMap ExchangeTokenRequest request);
}
