package com.evotek.storage.infrastructure.persistence.repository.custom;

import java.util.List;

import com.evotek.storage.domain.query.SearchFileQuery;
import com.evotek.storage.infrastructure.persistence.entity.FileEntity;

public interface FileEntityRepositoryCustom {
    List<FileEntity> search(SearchFileQuery searchFileQuery);

    Long count(SearchFileQuery searchFileQuery);
}
