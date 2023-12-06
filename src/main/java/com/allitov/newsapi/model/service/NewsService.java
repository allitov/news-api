package com.allitov.newsapi.model.service;

import com.allitov.newsapi.model.data.News;
import com.allitov.newsapi.web.filter.NewsFilter;

import java.util.List;

public interface NewsService {

    News findById(Long id);

    List<News> filterBy(NewsFilter filter);

    News save(News news);

    News update(News news);

    void deleteById(Long id);
}
