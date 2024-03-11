package com.allitov.newsapi.web.dto.response.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Ivan Ivanov")
    private String username;

    @Schema(example = "email@example")
    private String email;

    @Schema(example = "1970-01-01T00:00:00Z")
    private Instant regDate;
}
