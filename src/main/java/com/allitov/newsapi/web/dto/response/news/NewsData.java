package com.allitov.newsapi.web.dto.response.news;

import lombok.Data;

import java.time.Instant;

@Data
public class NewsData {

    private Long id;

    private String content;

    private Long authorId;

    private Long categoryId;

    private Instant creationDate;

    private Instant lastUpdate;
}
