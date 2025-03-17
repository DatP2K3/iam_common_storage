package com.evotek.storage.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;

import com.evotek.storage.domain.File;
import com.evotek.storage.infrastructure.persistence.entity.FileEntity;

@Mapper(componentModel = "Spring")
public interface FileEntityMapper extends EntityMapper<File, FileEntity> {}
