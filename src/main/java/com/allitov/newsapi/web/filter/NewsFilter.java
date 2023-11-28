package com.allitov.newsapi.web.filter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewsFilter {

    @NotNull(message = "Page size must be specified")
    @Positive(message = "Page size must be > 0")
    private Integer pageSize;

    @NotNull(message = "Page number must be specified")
    @PositiveOrZero(message = "Page number must be >= 0")
    private Integer pageNumber;

    private String category;

    private String author;
}
