package com.evo.common.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
public class PageApiResponse<T> extends ApiResponses<T> {
    private PageableResponse pageable;

    @Data
    @Builder
    public static class PageableResponse {
        private int pageIndex;
        private int pageSize;
    }
}