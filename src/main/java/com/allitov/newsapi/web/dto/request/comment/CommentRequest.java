package com.allitov.newsapi.web.dto.request.comment;

import com.allitov.newsapi.exception.ExceptionMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

    @NotNull(message = ExceptionMessage.COMMENT_NULL_NEWS_ID)
    @Schema(example = "1", minimum = "1")
    private Long newsId;

    @NotBlank(message = ExceptionMessage.COMMENT_BLANK_CONTENT)
    @Schema(example = "comment content", minLength = 1)
    private String content;
}
