package com.evotek.storage.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class FileStorageProperties {
    @Value("${file.upload-dir.public}")
    private String publicUploadDir;
    @Value("${file.upload-dir.private}")
    private String privateUploadDir;
    @Value("${file.allowed-types}")
    private String allowedTypes;
    @Value("${file.allowed-extensions}")
    private String allowedExtensions;
}