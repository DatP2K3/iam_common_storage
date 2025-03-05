package com.evotek.storage.infrastructure.persistence.repository.custom;

import com.evotek.storage.domain.query.SearchFileQuery;
import com.evotek.storage.infrastructure.persistence.entity.FileEntity;

import java.util.List;

public interface FileEntityRepositoryCustom {
    List<FileEntity> search(SearchFileQuery searchFileQuery);
    Long count(SearchFileQuery searchFileQuery);
}
