package com.allitov.newsapi.web.dto.request.newscategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewsCategoryRequest {

    @NotBlank(message = "Name must be specified")
    @Size(max = 50, message = "News category name must be <= {max}")
    private String name;
}
