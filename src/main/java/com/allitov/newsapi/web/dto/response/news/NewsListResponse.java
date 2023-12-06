package com.allitov.newsapi.web.dto.response.news;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NewsListResponse {

    private List<NewsWithCommentsCount> news = new ArrayList<>();
}
