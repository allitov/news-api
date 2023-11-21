package com.allitov.newsapi.model.repository;

import com.allitov.newsapi.model.data.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {
}
