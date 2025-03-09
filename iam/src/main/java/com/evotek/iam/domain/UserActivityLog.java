package com.evotek.iam.domain;

import java.util.UUID;

import com.evo.common.Auditor;
import com.evotek.iam.domain.command.WriteLogCmd;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class UserActivityLog extends Auditor {
    private UUID id;
    private String activity;

    public UserActivityLog(WriteLogCmd cmd) {
        this.activity = cmd.getActivity();
    }
}
