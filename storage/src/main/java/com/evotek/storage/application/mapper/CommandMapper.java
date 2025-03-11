package com.evotek.storage.application.mapper;

import org.mapstruct.Mapper;

import com.evotek.storage.application.dto.request.UpdateFileRequest;
import com.evotek.storage.domain.command.UpdateFileCmd;

@Mapper(componentModel = "spring")
public interface CommandMapper {
    UpdateFileCmd from(UpdateFileRequest request);
}
