package com.evotek.storage.domain.command;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class UpdateFileCmd {
    private UUID fileId;
    private String originName;
    private String description;
}
