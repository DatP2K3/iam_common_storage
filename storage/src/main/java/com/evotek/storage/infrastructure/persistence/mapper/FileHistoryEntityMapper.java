package com.evotek.storage.infrastructure.persistence.mapper;

import com.evotek.storage.domain.File;
import com.evotek.storage.domain.FileHistory;
import com.evotek.storage.infrastructure.persistence.entity.FileEntity;
import com.evotek.storage.infrastructure.persistence.entity.FileHistoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface FileHistoryEntityMapper extends EntityMapper<FileHistory, FileHistoryEntity>{
}
