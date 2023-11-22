package com.allitov.newsapi.web.dto.request.comment;

import lombok.Data;

@Data
public class CommentRequest {

    private Long authorId;

    private Long newsId;

    private String content;
}
