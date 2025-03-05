package com.evotek.storage.application.mapper;

import com.evotek.storage.application.dto.request.SearchFileRequest;
import com.evotek.storage.domain.query.SearchFileQuery;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QueryMapper {
    SearchFileQuery from(SearchFileRequest searchFileRequest);
}
