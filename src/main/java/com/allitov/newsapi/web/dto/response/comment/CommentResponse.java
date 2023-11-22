package com.allitov.newsapi.web.dto.response.comment;

import lombok.Data;

import java.time.Instant;

@Data
public class CommentResponse {

    private Long id;

    private String content;

    private Long authorId;

    private Long newsId;

    private Instant creationDate;

    private Instant lastUpdate;
}
