package com.evo.common;

import com.evo.common.iam.client.IamClient;
import com.evo.common.iam.client.IamNoTokenClient;
import com.evo.common.storage.client.StorageClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = {IamClient.class, IamNoTokenClient.class, StorageClient.class})
public class FeignClientAutoConfiguration {
}
