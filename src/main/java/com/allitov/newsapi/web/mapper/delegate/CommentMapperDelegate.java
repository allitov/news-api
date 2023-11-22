package com.allitov.newsapi.web.mapper.delegate;

import com.allitov.newsapi.model.data.Comment;
import com.allitov.newsapi.model.service.NewsService;
import com.allitov.newsapi.model.service.UserService;
import com.allitov.newsapi.web.dto.request.comment.CommentRequest;
import com.allitov.newsapi.web.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class CommentMapperDelegate implements CommentMapper {

    @Autowired
    private UserService userService;

    @Autowired
    private NewsService newsService;

    @Override
    public Comment requestToComment(CommentRequest request) {
        return Comment.builder()
                .content(request.getContent())
                .author(userService.findById(request.getAuthorId()))
                .news(newsService.findById(request.getNewsId()))
                .build();
    }

    @Override
    public Comment requestToComment(Long commentId, CommentRequest request) {
        Comment comment = requestToComment(request);
        comment.setId(commentId);

        return comment;
    }
}
