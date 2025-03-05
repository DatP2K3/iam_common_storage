package com.evotek.storage.application.dto.mapper;

import com.evo.common.dto.response.FileResponse;
import com.evotek.storage.domain.File;
import com.evotek.storage.infrastructure.persistence.entity.FileEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FileResponseMapper extends DTOMapper<FileResponse, File, FileEntity> {
    List<FileResponse> listDomainModelToListDTO(List<File> domainModel);
}
