package com.evotek.storage.infrastructure.domainrepository;

import com.evotek.storage.domain.File;
import com.evotek.storage.domain.FileHistory;
import com.evotek.storage.domain.query.SearchFileQuery;
import com.evotek.storage.domain.repository.FileDomainRepository;
import com.evotek.storage.infrastructure.persistence.entity.FileEntity;
import com.evotek.storage.infrastructure.persistence.entity.FileHistoryEntity;
import com.evotek.storage.infrastructure.persistence.mapper.FileEntityMapper;
import com.evotek.storage.infrastructure.persistence.mapper.FileHistoryEntityMapper;
import com.evotek.storage.infrastructure.persistence.repository.FileEntityRepository;
import com.evotek.storage.infrastructure.persistence.repository.FileHistoryEntityRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class FileDomainRepositoryImpl extends AbstractDomainRepository<File, FileEntity, UUID>
    implements FileDomainRepository {
    private final FileEntityMapper fileEntityMapper;
    private final FileEntityRepository fileEntityRepository;
    private final FileHistoryEntityMapper fileHistoryEntityMapper;
    private final FileHistoryEntityRepository fileHistoryEntityRepository;

    public FileDomainRepositoryImpl(FileEntityRepository fileEntityRepository,
                                    FileEntityMapper fileEntityMapper,
                                    FileHistoryEntityMapper fileHistoryEntityMapper,
                                    FileHistoryEntityRepository fileHistoryEntityRepository) {
        super(fileEntityRepository, fileEntityMapper);
        this.fileEntityRepository = fileEntityRepository;
        this.fileEntityMapper = fileEntityMapper;
        this.fileHistoryEntityMapper = fileHistoryEntityMapper;
        this.fileHistoryEntityRepository = fileHistoryEntityRepository;
    }

    @Override
    public List<File> search(SearchFileQuery searchFileQuery) {
        List<FileEntity> fileEntities = fileEntityRepository.search(searchFileQuery);
        return fileEntityMapper.toDomainModelList(fileEntities);
    }

    @Override
    public Long count(SearchFileQuery searchFileQuery) {
        return fileEntityRepository.count(searchFileQuery);
    }

    @Override
    public List<File> saveAll(List<File> domains) {
        List<FileHistory> fileHistories = domains.stream()
                .map(File::getHistory)
                .toList();
        List<FileEntity> fileEntities = fileEntityMapper.toEntityList(domains);
        List<FileHistoryEntity> fileHistoryEntities = fileHistoryEntityMapper.toEntityList(fileHistories);
        fileHistoryEntityRepository.saveAll(fileHistoryEntities);
        return fileEntityMapper.toDomainModelList(fileEntityRepository.saveAll(fileEntities));
    }
}
