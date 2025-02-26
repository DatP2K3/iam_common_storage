package com.evotek.iam.application.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchUserRequest extends PagingRequest {
    private String keyword;
}

