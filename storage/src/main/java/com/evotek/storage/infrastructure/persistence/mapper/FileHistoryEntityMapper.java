package com.evotek.storage.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;

import com.evotek.storage.domain.FileHistory;
import com.evotek.storage.infrastructure.persistence.entity.FileHistoryEntity;

@Mapper(componentModel = "Spring")
public interface FileHistoryEntityMapper extends EntityMapper<FileHistory, FileHistoryEntity> {}
