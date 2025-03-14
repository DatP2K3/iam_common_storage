package com.evotek.elasticsearch.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchUserRequest {
    private String keyword;
    private UUID roleId;
    private Boolean locked;
    private int page;
    private int size;
    private String sortField;
    private String sortDirection;
    private Integer minExperience;
    private Integer maxExperience;
}
