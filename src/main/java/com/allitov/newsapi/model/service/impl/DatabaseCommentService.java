package com.allitov.newsapi.model.service.impl;

import com.allitov.newsapi.exception.ExceptionMessage;
import com.allitov.newsapi.model.data.Comment;
import com.allitov.newsapi.model.repository.CommentRepository;
import com.allitov.newsapi.model.repository.specification.CommentSpecification;
import com.allitov.newsapi.model.service.CommentService;
import com.allitov.newsapi.util.BeanUtils;
import com.allitov.newsapi.web.filter.CommentFilter;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseCommentService implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public Comment findById(Long id) {
        return commentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(ExceptionMessage.COMMENT_BY_ID_NOT_FOUND, id))
        );
    }

    @Override
    public List<Comment> filterBy(CommentFilter filter) {
        return commentRepository.findAll(CommentSpecification.withFilter(filter));
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment update(Comment comment) {
        Comment foundComment = findById(comment.getId());
        BeanUtils.copyNonNullProperties(comment, foundComment);

        return commentRepository.save(foundComment);
    }

    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
