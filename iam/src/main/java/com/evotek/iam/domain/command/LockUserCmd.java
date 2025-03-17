package com.evotek.iam.domain.command;

import lombok.*;

@Getter
@Setter
@Builder
public class LockUserCmd {
    private boolean enabled;
}
