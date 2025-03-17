package com.evotek.storage.application.service;

import java.util.List;
import java.util.UUID;

import com.evo.common.dto.response.FileResponse;
import com.evo.common.dto.response.PageApiResponse;
import com.evotek.storage.application.dto.request.SearchFileRequest;

public interface FileQueryService {
    PageApiResponse<List<FileResponse>> search(SearchFileRequest searchFileRequest);

    FileResponse getPrivateFile(UUID filedId);

    FileResponse getPublicFile(UUID filedId);
}
