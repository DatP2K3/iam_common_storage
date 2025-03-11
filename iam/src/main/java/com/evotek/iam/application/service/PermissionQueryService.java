package com.evotek.iam.application.service;

import java.util.List;

import com.evotek.iam.application.dto.request.SearchPermissionRequest;
import com.evotek.iam.application.dto.response.PermissionDTO;

public interface PermissionQueryService {
    List<PermissionDTO> search(SearchPermissionRequest searchPermissionRequest);

    Long totalPermissions(SearchPermissionRequest searchPermissionRequest);
}
