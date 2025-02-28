package com.evo.common.dto.request;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchFileRequest {
    @Min(value = 1, message = "Page index must be greater than 0")
    @Max(value = 1000, message = "Page index be less than 1000")
    protected int pageIndex = 1;

    @Min(value = 1, message = "Page size must be greater than 0")
    @Max(value = 500, message = "Page size must be less than or equal to 500")
    protected int pageSize = 30;

    @NotNull(message = "Keyword must not be null")
    protected String keyword;

    protected String sortBy;
}
