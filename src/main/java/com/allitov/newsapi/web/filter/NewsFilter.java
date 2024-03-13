package com.allitov.newsapi.web.filter;

import com.allitov.newsapi.exception.ExceptionMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewsFilter {

    @NotNull(message = ExceptionMessage.FILTER_NULL_PAGE_SIZE)
    @Positive(message = ExceptionMessage.FILTER_INVALID_PAGE_SIZE)
    @Schema(example = "1", description = "Page size must be > 0")
    private Integer pageSize;

    @NotNull(message = ExceptionMessage.FILTER_NULL_PAGE_NUMBER)
    @PositiveOrZero(message = ExceptionMessage.FILTER_INVALID_PAGE_NUMBER)
    @Schema(example = "0", description = "Page number must be >= 0")
    private Integer pageNumber;

    @Schema(example = "news category name")
    private String category;

    @Schema(example = "news author name")
    private String author;
}
