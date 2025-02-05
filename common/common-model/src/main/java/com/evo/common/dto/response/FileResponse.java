package com.evo.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileResponse {
    private int id;
    private String originName;
    private String md5Name;
    private String fileType;
    private Integer fileHeight;
    private Integer fileWidth;
    private Long fileSize;
    private String url;
    private String description;
    private Boolean isPublic;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String lastModifiedBy;
}
