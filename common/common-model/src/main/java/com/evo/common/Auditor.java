package com.evo.common;

import java.time.Instant;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class Auditor {
    protected String createdBy;
    protected String lastModifiedBy;
    protected Instant createdAt;
}
