package com.allitov.newsapi.model.service;

import com.allitov.newsapi.model.data.NewsCategory;
import com.allitov.newsapi.web.filter.NewsCategoryFilter;

import java.util.List;

public interface NewsCategoryService {

    NewsCategory findById(Long id);

    List<NewsCategory> filterBy(NewsCategoryFilter filter);

    NewsCategory save(NewsCategory newsCategory);

    NewsCategory update(NewsCategory newsCategory);

    void deleteById(Long id);
}
