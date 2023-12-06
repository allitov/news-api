package com.allitov.newsapi.web.dto.response.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class UserResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Ivan Ivanov")
    private String name;

    @Schema(example = "email@example")
    private String email;

    @Schema(example = "1970-01-01T00:00:00Z")
    private Instant regDate;
}
