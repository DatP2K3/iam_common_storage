package com.evotek.storage.domain;

import com.evotek.storage.domain.command.StoreFileCmd;
import com.evotek.storage.domain.command.UpdateFileCmd;
import com.evotek.storage.infrastructure.support.IdUtils;
import com.evotek.storage.infrastructure.support.exception.AppErrorCode;
import com.evotek.storage.infrastructure.support.exception.AppException;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class File {
    private UUID id;
    private String originName;
    private String md5Name;
    private String fileType;
    private Integer fileHeight;
    private Integer fileWidth;
    private Long fileSize;
    private String description;
    private String url;
    private Boolean isPublic;
    private Boolean deleted = false;
    private String createdBy;
    private String lastModifiedBy;
    private LocalDateTime createdAt;
    private FileHistory history;

    public File (StoreFileCmd cmd) {
        validateFileName(cmd.getOriginName());
        this.id = IdUtils.nextId();
        this.originName = cmd.getOriginName();
        this.md5Name = hashFileName(cmd.getOriginName());
        this.fileType = cmd.getFileType();
        this.fileHeight = cmd.getFileHeight();
        this.fileWidth = cmd.getFileWidth();
        this.fileSize = cmd.getFileSize();
        this.description = cmd.getDescription();
        this.isPublic = cmd.getIsPublic();
        if (isPublic) {
            this.url = "http://localhost:8080/api/public/file/" + this.id;
        } else {
            this.url = "http://localhost:8080/api/file/" + this.id;
        }
    }

    public void update(UpdateFileCmd cmd) {
        validateFileName(cmd.getOriginName());
        this.originName = cmd.getOriginName();
        this.md5Name = hashFileName(this.originName);
        this.description = cmd.getDescription();
        if (originName == null || originName.contains("..")) {
            throw new RuntimeException("Invalid file name: " + originName);
        }
    }

    private String hashFileName(String fileName) {
        try {
            String fileExtension = originName.substring(originName.lastIndexOf("."));
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] hashBytes = messageDigest.digest(fileName.getBytes(StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : hashBytes) {
                stringBuilder.append(String.format("%02x", b));
            }
            return stringBuilder + fileExtension;
        } catch (NoSuchAlgorithmException e) {
            throw new AppException(AppErrorCode.CANT_HASH_FILE_NAME);
        }
    }

    private void validateFileName(String fileName) {
        if(fileName == null || fileName.contains("..")) {
            throw new AppException(AppErrorCode.INVALID_FILENAME);
        }
    }
}
