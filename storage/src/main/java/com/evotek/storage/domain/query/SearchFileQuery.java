package com.evotek.storage.domain.query;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchFileQuery extends PagingQuery {
    private String keyword;
}
