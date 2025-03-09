package com.evotek.iam.application.mapper;

import org.mapstruct.Mapper;

import com.evotek.iam.application.dto.request.SearchPermissionRequest;
import com.evotek.iam.application.dto.request.SearchUserRequest;
import com.evotek.iam.domain.query.SearchPermissionQuery;
import com.evotek.iam.domain.query.SearchUserQuery;

@Mapper(componentModel = "spring")
public interface QueryMapper {
    SearchPermissionQuery from(SearchPermissionRequest request);

    SearchUserQuery from(SearchUserRequest request);
}
