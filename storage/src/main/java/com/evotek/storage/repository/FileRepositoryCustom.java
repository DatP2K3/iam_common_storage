package com.evotek.storage.repository;

import com.evotek.storage.dto.request.FileSearchRequest;
import com.evotek.storage.model.File;

import java.util.List;

public interface FileRepositoryCustom {

    List<File> search(FileSearchRequest fileSearchRequest);
}
