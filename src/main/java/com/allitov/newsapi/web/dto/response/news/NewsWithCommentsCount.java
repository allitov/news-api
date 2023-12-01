package com.allitov.newsapi.web.dto.response.news;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class NewsWithCommentsCount {

    private Long id;

    private String content;

    private Long authorId;

    private Long categoryId;

    private Instant creationDate;

    private Instant lastUpdate;

    private Integer commentsCount;
}
