package com.evotek.elasticsearch.application.service.impl;

import com.evotek.elasticsearch.application.dto.request.SearchUserRequest;
import com.evotek.elasticsearch.application.dto.response.SearchUserResponse;

public interface UserQueryService {
    SearchUserResponse searchUser(SearchUserRequest request);
}
