package com.evotek.iam.domain.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Getter
@Setter
@Builder
@JsonIgnoreProperties(
        value = {"type", "temporary"},
        allowSetters = true)
public class ResetKeycloakPasswordCmd {
    private String type = "password";
    private String value;
    private boolean temporary = false;
}
