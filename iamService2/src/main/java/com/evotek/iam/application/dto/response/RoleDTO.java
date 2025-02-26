package com.evotek.iam.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    private UUID id;
    private String name;
    private String description;
    private boolean isRoot;
    private String createdBy;
    private String lastModifiedBy;
    private LocalDateTime createdAt;
}
