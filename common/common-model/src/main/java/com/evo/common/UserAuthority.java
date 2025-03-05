package com.evo.common;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAuthority {
    private UUID userId;
    private Boolean isRoot;
    private Boolean isClient;
    private List<String> grantedPermissions;
}
