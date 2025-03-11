package com.evotek.storage.domain;

import java.util.UUID;

import com.evo.common.Auditor;
import com.evotek.storage.domain.command.WriteHistoryCmd;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class FileHistory extends Auditor {
    private UUID fileId;
    private String action;

    public FileHistory(WriteHistoryCmd cmd) {
        this.fileId = cmd.getFileId();
        this.action = cmd.getAction();
    }
}
