package com.evotek.iam.application.dto.response;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDTO {
    private UUID id;
    private String resourceId;
    private String scope;
    private String createdBy;
    private String lastModifiedBy;
    private Instant createdAt;
}
