package com.evotek.iam.application.dto.request;

import com.evo.common.dto.request.PagingRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchPermissionRequest extends PagingRequest {
    private String keyword;
}
