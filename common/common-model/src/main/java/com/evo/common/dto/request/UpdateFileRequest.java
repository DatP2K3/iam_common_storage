package com.evo.common.dto.request;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateFileRequest {
    private UUID fileId;
    private String originName;
    private String description;
}
