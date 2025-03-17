package com.evotek.storage.domain.command;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StoreFileCmd {
    private String originName;
    private String fileType;
    private int fileHeight;
    private int fileWidth;
    private Long fileSize;
    private String description;
    private String url;
    private Boolean isPublic;
}
