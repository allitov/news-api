package com.allitov.newsapi.web.dto.response.news;

import com.allitov.newsapi.web.dto.response.comment.CommentResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsResponse {

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

    private List<CommentResponse> comments;
}
