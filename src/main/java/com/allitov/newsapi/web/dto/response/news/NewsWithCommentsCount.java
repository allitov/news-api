package com.allitov.newsapi.web.dto.response.news;

import lombok.Data;

@Data
public class NewsWithCommentsCount {

    private NewsData news;

    private Integer commentsCount;
}
