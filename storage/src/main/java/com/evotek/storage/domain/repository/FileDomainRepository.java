package com.evotek.storage.domain.repository;

import java.util.List;
import java.util.UUID;

import com.evotek.storage.domain.File;
import com.evotek.storage.domain.query.SearchFileQuery;
import com.evotek.storage.infrastructure.domainrepository.DomainRepository;

public interface FileDomainRepository extends DomainRepository<File, UUID> {
    List<File> search(SearchFileQuery searchFileQuery);

    Long count(SearchFileQuery searchFileQuery);
}
