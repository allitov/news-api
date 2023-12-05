package com.allitov.newsapi.web.dto.request.newscategory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewsCategoryRequest {

    @NotBlank(message = "Name must be specified")
    @Size(min = 1, max = 50, message = "News category name must be <= {max}")
    @Schema(example = "News category", minLength = 1)
    private String name;
}
