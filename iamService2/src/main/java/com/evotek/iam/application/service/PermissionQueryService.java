package com.evotek.iam.application.service;

import com.evotek.iam.application.dto.request.SearchPermissionRequest;
import com.evotek.iam.application.dto.response.PermissionDTO;

import java.util.List;

public interface PermissionQueryService {
    List<PermissionDTO> search(SearchPermissionRequest searchPermissionRequest);
    Long totalPermissions(SearchPermissionRequest searchPermissionRequest);
}
