package com.allitov.newsapi.web.dto.request.newscategory;

import com.allitov.newsapi.exception.ExceptionMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewsCategoryRequest {

    @NotBlank(message = ExceptionMessage.NEWS_CATEGORY_BLANK_NAME)
    @Size(min = 1, max = 50, message = ExceptionMessage.NEWS_CATEGORY_INVALID_NAME)
    @Schema(example = "News category", minLength = 1)
    private String name;
}
