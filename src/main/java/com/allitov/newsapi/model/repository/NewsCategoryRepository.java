package com.allitov.newsapi.model.repository;

import com.allitov.newsapi.model.data.NewsCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsCategoryRepository extends JpaRepository<NewsCategory, Long> {
}
