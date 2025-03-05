package com.evotek.iam.application.mapper;

import com.evotek.iam.application.dto.request.SearchPermissionRequest;
import com.evotek.iam.application.dto.request.SearchUserRequest;
import com.evotek.iam.domain.query.SearchPermissionQuery;
import com.evotek.iam.domain.query.SearchUserQuery;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QueryMapper {
    SearchPermissionQuery from(SearchPermissionRequest request);
    SearchUserQuery from(SearchUserRequest request);
}
