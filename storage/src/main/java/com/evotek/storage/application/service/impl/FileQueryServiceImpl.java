package com.evotek.storage.application.service.impl;

import com.evo.common.dto.response.FileResponse;
import com.evo.common.dto.response.PageApiResponse;
import com.evotek.storage.application.dto.mapper.FileResponseMapper;
import com.evotek.storage.application.mapper.QueryMapper;
import com.evotek.storage.application.service.FileQueryService;
import com.evotek.storage.application.dto.request.SearchFileRequest;
import com.evotek.storage.domain.File;
import com.evotek.storage.domain.query.SearchFileQuery;
import com.evotek.storage.domain.repository.FileDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileQueryServiceImpl implements FileQueryService {
    private final FileDomainRepository fileDomainRepository;
    private final FileResponseMapper fileResponseMapper;
    private final QueryMapper queryMapper;

    @Override
    public PageApiResponse<List<FileResponse>> search(SearchFileRequest request) {
        SearchFileQuery searchFileQuery = queryMapper.from(request);
        List<File> files = fileDomainRepository.search(searchFileQuery);
        Long totalFiles = fileDomainRepository.count(searchFileQuery);
        List <FileResponse> fileResponses = files.stream().map(fileResponseMapper::domainModelToDTO).toList();
        PageApiResponse.PageableResponse pageableResponse = PageApiResponse.PageableResponse.builder()
                .pageSize(request.getPageSize())
                .pageIndex(request.getPageIndex())
                .totalElements(totalFiles)
                .totalPages((int)(Math.ceil((double)totalFiles / request.getPageSize())))
                .hasNext(((request.getPageIndex() + 1L) * request.getPageSize() < totalFiles))
                .hasPrevious(request.getPageIndex() >0).build();
        return PageApiResponse.<List<FileResponse>>builder()
                .data(fileResponses)
                .success(true)
                .code(200)
                .pageable(pageableResponse)
                .message("Search File successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }

    @Override
    public FileResponse getPrivateFile(UUID filedId) {
        File file = fileDomainRepository.findById(filedId)
                .orElseThrow(() -> new RuntimeException("File not found with id " + filedId));
        String url = "http://localhost:8080/api/uploads/private/" + file.getMd5Name();
        FileResponse fileResponse = fileResponseMapper.domainModelToDTO(file);
        fileResponse.setUrl(url);
        return fileResponse;
    }

    @Override
    public FileResponse getPublicFile(UUID filedId) {
        File file = fileDomainRepository.findById(filedId)
                .orElseThrow(() -> new RuntimeException("File not found with id " + filedId));
        String url = "http://localhost:8080/api/uploads/public/" + file.getMd5Name();
        FileResponse fileResponse = fileResponseMapper.domainModelToDTO(file);
        fileResponse.setUrl(url);
        return fileResponse;
    }
}
