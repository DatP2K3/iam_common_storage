package com.evotek.elasticsearch.application.dto.response;

import com.evotek.elasticsearch.domain.User;
import com.evotek.elasticsearch.infrastructure.persistence.document.UserDocument;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
public class SearchUserResponse {
    List<User> users;
    private int pageIndex;
    private int pageSize;
    private int totalPages;
    private long totalElements;
    private boolean hasNext;
    private boolean hasPrevious;
}
