package com.evotek.storage.domain.repository;

import com.evotek.storage.domain.File;
import com.evotek.storage.domain.query.SearchFileQuery;
import com.evotek.storage.infrastructure.domainrepository.DomainRepository;

import java.util.List;
import java.util.UUID;

public interface FileDomainRepository extends DomainRepository<File, UUID> {
    List<File> search(SearchFileQuery searchFileQuery);
    Long count(SearchFileQuery searchFileQuery);
}
