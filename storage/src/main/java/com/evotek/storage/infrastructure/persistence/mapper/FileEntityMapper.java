package com.evotek.storage.infrastructure.persistence.mapper;

import com.evotek.storage.domain.File;
import com.evotek.storage.infrastructure.persistence.entity.FileEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface FileEntityMapper extends EntityMapper<File, FileEntity>{
}
