package com.evotek.storage.domain.command;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateFileCmd {
    private UUID fileId;
    private String originName;
    private String description;
}
