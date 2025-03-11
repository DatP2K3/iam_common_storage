package com.evotek.storage.domain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import com.evo.common.Auditor;
import com.evotek.storage.domain.command.StoreFileCmd;
import com.evotek.storage.domain.command.UpdateFileCmd;
import com.evotek.storage.infrastructure.support.IdUtils;
import com.evotek.storage.infrastructure.support.exception.AppErrorCode;
import com.evotek.storage.infrastructure.support.exception.AppException;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class File extends Auditor {
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
    private FileHistory history;

    public File(StoreFileCmd cmd) {
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
        if (Boolean.TRUE.equals(isPublic)) {
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
            throw new AppException(AppErrorCode.INVALID_FILENAME);
        }
    }

    private String hashFileName(String fileName) {
        try {
            String fileExtension = originName.substring(originName.lastIndexOf("."));
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
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
        if (fileName == null || fileName.contains("..")) {
            throw new AppException(AppErrorCode.INVALID_FILENAME);
        }
    }
}
