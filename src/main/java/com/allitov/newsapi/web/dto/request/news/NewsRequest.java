package com.allitov.newsapi.web.dto.request.news;

import lombok.Data;

@Data
public class NewsRequest {

    private String content;

    private Long authorId;

    private Long categoryId;
}
