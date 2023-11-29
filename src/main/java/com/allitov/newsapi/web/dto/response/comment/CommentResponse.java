package com.allitov.newsapi.web.dto.response.comment;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class CommentResponse {

    private Long id;

    private String content;

    private Long authorId;

    private Long newsId;

    private Instant creationDate;

    private Instant lastUpdate;
}
