package com.allitov.newsapi.web.dto.request.news;

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
public class NewsRequest {

    @NotBlank(message = ExceptionMessage.NEWS_BLANK_CONTENT)
    @Schema(example = "news content", minLength = 1)
    private String content;

    @NotNull(message = ExceptionMessage.NEWS_NULL_CATEGORY_ID)
    @Schema(example = "1")
    private Long categoryId;
}
