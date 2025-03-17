package com.evotek.elasticsearch.infrastructure.domainrepository;

import java.util.List;
import java.util.UUID;

import com.evotek.elasticsearch.infrastructure.support.exception.AppErrorCode;
import com.evotek.elasticsearch.infrastructure.support.exception.AppException;
import org.springframework.stereotype.Repository;

import com.evotek.elasticsearch.domain.User;
import com.evotek.elasticsearch.domain.repository.UserDomainRepository;
import com.evotek.elasticsearch.infrastructure.persistence.document.UserDocument;
import com.evotek.elasticsearch.infrastructure.persistence.mapper.UserDocumentMapper;
import com.evotek.elasticsearch.infrastructure.persistence.repository.UserDocumentRepository;

@Repository
public class UserDomainRepositoryImpl extends AbstractDocumentDomainRepository<User, UserDocument, UUID>
        implements UserDomainRepository {
    private final UserDocumentMapper userDocumentMapper;
    private final UserDocumentRepository userDocumentRepository;

    public UserDomainRepositoryImpl(
            UserDocumentMapper userDocumentMapper, UserDocumentRepository userDocumentRepository) {
        super(userDocumentRepository, userDocumentMapper);
        this.userDocumentRepository = userDocumentRepository;
        this.userDocumentMapper = userDocumentMapper;
    }

    @Override
    public List<User> saveAll(List<User> domains) {
        List<UserDocument> userDocuments = userDocumentMapper.toDocumentList(domains);
        return userDocumentMapper.toDomainModelList(userDocumentRepository.saveAll(userDocuments));
    }

    @Override
    public User getById(UUID userId) {
        UserDocument userDocument = userDocumentRepository.findById(userId).orElseThrow(() -> new AppException(AppErrorCode.USER_NOT_FOUND));
        return userDocumentMapper.toDomainModel(userDocument);
    }

    @Override
    public void deleteById(UUID userId) {
        userDocumentRepository.deleteById(userId);
    }
}
