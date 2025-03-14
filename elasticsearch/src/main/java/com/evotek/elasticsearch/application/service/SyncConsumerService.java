package com.evotek.elasticsearch.application.service;

import com.evo.common.dto.event.SyncUserEvent;
import com.evotek.elasticsearch.application.mapper.CommandMapper;
import com.evotek.elasticsearch.application.service.impl.command.UserCommandServiceImpl;
import com.evotek.elasticsearch.domain.command.SyncUserCmd;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SyncConsumerService {
    private final UserCommandServiceImpl userCommandServiceImpl;
    private final CommandMapper commandMapper;

    @KafkaListener(topics = "sync-user-group")
    public void syncUser(SyncUserEvent event) {
        SyncUserCmd syncUserCmd = commandMapper.from(event.getSyncUserRequest());
        switch (event.getSyncUserType()) {
            case USER_CREATED:
                userCommandServiceImpl.create(syncUserCmd);
                break;
            case USER_UPDATED:
                userCommandServiceImpl.update(syncUserCmd);
                break;
            case USER_DELETED:
                userCommandServiceImpl.delete(syncUserCmd.getSelfUserID());
                break;
            default:
                log.error("Invalid sync user type: {}", event.getSyncUserType());
        }
    }
}
