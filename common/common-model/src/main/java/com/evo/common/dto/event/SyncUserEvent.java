package com.evo.common.dto.event;

import com.evo.common.dto.request.SyncUserRequest;
import com.evo.common.enums.SyncUserType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncUserEvent {
    SyncUserType syncUserType;
    SyncUserRequest syncUserRequest;
}
