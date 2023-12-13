package com.allitov.newsapi.model.repository;

import com.allitov.newsapi.model.data.News;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Long>, JpaSpecificationExecutor<News> {

    @Query("select n.id from News n")
    Page<Long> getAllIds(Pageable pageable);

    @Override
    @Nonnull
    @EntityGraph(attributePaths = {"comments"})
    Optional<News> findById(@Nonnull Long aLong);

    @Override
    @Nonnull
    @EntityGraph(attributePaths = {"comments"})
    List<News> findAll(@Nonnull Specification<News> spec);
}
