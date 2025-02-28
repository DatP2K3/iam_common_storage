package com.evo.common.iam.config;

import com.evo.common.dto.response.ApiResponses;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;


@Slf4j
public class FeignClientInterceptor implements RequestInterceptor {
    @Value("${oauth.client.iam.client-id}")
    private String clientId;
    @Value("${oauth.client.iam.client-secret}")
    private String clientSecret;


    @Override
    public void apply(RequestTemplate requestTemplate) {
        try {
            WebClient webClient = WebClient.builder().baseUrl("http://localhost:8888/api").build();
            // Tạo request body cần gửi (ví dụ body chứa clientId và clientSecret)
            String requestBody = "{\"clientSecret\": \"" + clientSecret + "\"}";
            // Sử dụng POST request với body
            ApiResponses<String> tokenResponse = webClient.post()
                    .uri("/client/token/" + clientId)  // Sử dụng uri cho POST request
                    .header("Content-Type", "application/json")  // Thiết lập header nếu cần
                    .bodyValue(requestBody)  // Thêm body vào request
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ApiResponses<String>>() {})
                    .block();  // Lấy kết quả trả về từ request

            if (tokenResponse == null || tokenResponse.getData() == null) {
                throw new RuntimeException("Failed to get client token from IAM service");
            }
            requestTemplate.header("Authorization", "Bearer " + tokenResponse.getData());
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Failed to get token from IAM service", e);
        }
    }
}
