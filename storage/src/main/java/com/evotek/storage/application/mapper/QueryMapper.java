package com.evotek.storage.application.mapper;

import org.mapstruct.Mapper;

import com.evotek.storage.application.dto.request.SearchFileRequest;
import com.evotek.storage.domain.query.SearchFileQuery;

@Mapper(componentModel = "spring")
public interface QueryMapper {
    SearchFileQuery from(SearchFileRequest searchFileRequest);
}
