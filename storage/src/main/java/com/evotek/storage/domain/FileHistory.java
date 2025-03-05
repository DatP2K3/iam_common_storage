package com.evotek.storage.domain;

import com.evotek.storage.domain.command.WriteHistoryCmd;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class FileHistory {
    private UUID fileId;
    private String action;

    public FileHistory(WriteHistoryCmd cmd) {
        this.fileId = cmd.getFileId();
        this.action = cmd.getAction();
    }
}
