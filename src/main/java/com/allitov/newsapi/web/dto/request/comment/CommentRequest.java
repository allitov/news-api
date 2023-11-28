package com.allitov.newsapi.web.dto.request.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CommentRequest {

    @NotNull(message = "Author id must be specified")
    @Positive(message = "Author id must be > 0")
    private Long authorId;

    @NotNull(message = "News id must be specified")
    @Positive(message = "News id must be > 0")
    private Long newsId;

    @NotBlank(message = "Content must be specified")
    private String content;
}
