package com.allitov.newsapi.web.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewsCategoryFilter {

    @NotNull(message = "Page size must be specified")
    @Positive(message = "Page size must be > 0")
    @Schema(example = "1", description = "Page size must be > 0")
    private Integer pageSize;

    @NotNull(message = "Page number must be specified")
    @PositiveOrZero(message = "Page number must be >= 0")
    @Schema(example = "0", description = "Page number must be >= 0")
    private Integer pageNumber;
}
