package com.evotek.storage.application.mapper;

import com.evotek.storage.application.dto.request.UpdateFileRequest;
import com.evotek.storage.domain.command.UpdateFileCmd;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommandMapper {
    UpdateFileCmd from(UpdateFileRequest request);

}
