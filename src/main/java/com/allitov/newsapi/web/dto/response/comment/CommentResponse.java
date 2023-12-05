package com.allitov.newsapi.web.dto.response.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class CommentResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "comment content")
    private String content;

    @Schema(example = "1")
    private Long authorId;

    @Schema(example = "1")
    private Long newsId;

    @Schema(example = "1970-01-01T00:00:00Z")
    private Instant creationDate;

    @Schema(example = "1970-01-01T00:00:00Z")
    private Instant lastUpdate;
}
