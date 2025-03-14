package com.evotek.elasticsearch.application.service.impl.command;

import java.util.UUID;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import com.evotek.elasticsearch.application.service.impl.UserCommandService;
import com.evotek.elasticsearch.domain.User;
import com.evotek.elasticsearch.domain.command.SyncUserCmd;
import com.evotek.elasticsearch.domain.repository.UserDomainRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {
    private final ElasticsearchOperations elasticsearchOperations;
    private final UserDomainRepository userDomainRepository;

    @Override
    public void create(SyncUserCmd syncUserCmd) {
        User user = new User(syncUserCmd);
        userDomainRepository.save(user);
    }

    @Override
    public void update(SyncUserCmd syncUserCmd) {
        User user = userDomainRepository.getById(syncUserCmd.getSelfUserID());
        user.update(syncUserCmd);
        userDomainRepository.save(user);
    }

    @Override
    public void delete(UUID selfUserID) {
        userDomainRepository.deleteById(selfUserID);
    }
}
