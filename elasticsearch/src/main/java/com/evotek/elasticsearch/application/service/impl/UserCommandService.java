package com.evotek.elasticsearch.application.service.impl;

import com.evotek.elasticsearch.domain.command.SyncUserCmd;

import java.util.UUID;

public interface UserCommandService {
    void create(SyncUserCmd syncUserCmd);
    void update(SyncUserCmd syncUserCmd);
    void delete(UUID selfUserID);
}
