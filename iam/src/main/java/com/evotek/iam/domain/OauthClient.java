package com.evotek.iam.domain;

import java.util.UUID;

import com.evo.common.Auditor;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class OauthClient extends Auditor {
    private UUID id;
    private String clientId;
    private String clientSecret;
    private String clientName;
}
