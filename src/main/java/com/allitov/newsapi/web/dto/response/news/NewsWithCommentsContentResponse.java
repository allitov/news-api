package com.allitov.newsapi.web.dto.response.news;

import com.allitov.newsapi.web.dto.response.comment.CommentListResponse;
import lombok.Data;

@Data
public class NewsWithCommentsContentResponse {

    private NewsData news;

    private CommentListResponse comments;
}
