package com.allitov.newsapi.web.dto.response.news;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class NewsWithCommentsCount {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "news content")
    private String content;

    @Schema(example = "1")
    private Long authorId;

    @Schema(example = "1")
    private Long categoryId;

    @Schema(example = "1970-01-01T00:00:00Z")
    private Instant creationDate;

    @Schema(example = "1970-01-01T00:00:00Z")
    private Instant lastUpdate;

    @Schema(example = "1")
    private Integer commentsCount;
}
