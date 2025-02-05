package com.evotek.storage.mapper;

import com.evo.common.dto.response.FileResponse;
import com.evotek.storage.model.File;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMapper {
    FileResponse fileToFileResponse(File file);
}
