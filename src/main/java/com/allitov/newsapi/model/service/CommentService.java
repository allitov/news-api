package com.allitov.newsapi.model.service;

import com.allitov.newsapi.model.data.Comment;
import com.allitov.newsapi.web.filter.CommentFilter;

import java.util.List;

public interface CommentService {

    Comment findById(Long id);

    List<Comment> filterBy(CommentFilter filter);

    Comment save(Comment comment);

    Comment update(Comment comment);

    void deleteById(Long id);
}
