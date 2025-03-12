package com.evo.common.dto.request;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTopicsOfUserRequest {
    private UUID userId;
    private List<String> topics = new ArrayList<>();
}
