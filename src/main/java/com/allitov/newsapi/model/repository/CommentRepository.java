package com.allitov.newsapi.model.repository;

import com.allitov.newsapi.model.data.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
