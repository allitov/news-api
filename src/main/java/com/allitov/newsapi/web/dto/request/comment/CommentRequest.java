package com.allitov.newsapi.web.dto.request.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CommentRequest {

    @NotNull(message = "Author id must be specified")
    @Positive(message = "Author id must be > 0")
    @Schema(example = "1", minimum = "1")
    private Long authorId;

    @NotNull(message = "News id must be specified")
    @Positive(message = "News id must be > 0")
    @Schema(example = "1", minimum = "1")
    private Long newsId;

    @NotBlank(message = "Content must be specified")
    @Schema(example = "comment content", minLength = 1)
    private String content;
}
