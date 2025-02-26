package com.evotek.iam.domain.command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordCmd {
    private String oldPassword;
    private String newPassword;
}
