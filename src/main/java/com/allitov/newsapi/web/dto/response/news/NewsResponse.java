package com.allitov.newsapi.web.dto.response.news;

import com.allitov.newsapi.web.dto.response.comment.CommentResponse;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class NewsResponse {

    private Long id;

    private String content;

    private Long authorId;

    private Long categoryId;

    private Instant creationDate;

    private Instant lastUpdate;

    private List<CommentResponse> comments;
}
