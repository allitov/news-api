package com.allitov.newsapi.web.filter;

import com.allitov.newsapi.exception.ExceptionMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentFilter {

    @NotNull(message = ExceptionMessage.COMMENT_FILTER_NULL_NEWS_ID)
    @Schema(example = "1", description = "News id must be > 0")
    private Long newsId;
}
