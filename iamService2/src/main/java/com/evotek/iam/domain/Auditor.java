package com.evotek.iam.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class Auditor {
    protected String createdBy;
    protected String lastModifiedBy;
    protected LocalDateTime createdAt;
}
