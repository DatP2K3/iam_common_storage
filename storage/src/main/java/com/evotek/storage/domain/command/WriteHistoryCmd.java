package com.evotek.storage.domain.command;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class WriteHistoryCmd {
    private UUID fileId;
    private String action;
}
