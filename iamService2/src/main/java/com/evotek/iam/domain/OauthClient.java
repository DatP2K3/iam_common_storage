package com.evotek.iam.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
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
