package com.evo.common.config;

import com.evo.common.dto.response.ApiResponses;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
        WebClient webClient = WebClient.builder().baseUrl("http://localhost:8888/api").build();

        // Tạo request body cần gửi (ví dụ body chứa clientId và clientSecret)
        String requestBody = "{\"clientSecret\": \"" + clientSecret + "\"}";

        try {
            // Sử dụng POST request với body
            ApiResponses<String> token = webClient.post()
                    .uri("/client/token/" + clientId)  // Sử dụng uri cho POST request
                    .header("Content-Type", "application/json")  // Thiết lập header nếu cần
                    .bodyValue(requestBody)  // Thêm body vào request
                    .retrieve()
                    .bodyToMono(ApiResponses.class)
                    .block();  // Lấy kết quả trả về từ request

            System.out.println("======================Token: " + token.getData());
            requestTemplate.header("Authorization", "Bearer " + token.getData());
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Failed to get token from IAM service", e);
        }
    }
}
