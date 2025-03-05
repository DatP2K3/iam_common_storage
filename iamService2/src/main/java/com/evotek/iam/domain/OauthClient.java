package com.evotek.iam.domain;

import com.evo.common.Auditor;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

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
