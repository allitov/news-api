package com.allitov.newsapi.web.dto.request.newscategory;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewsCategoryRequest {

    @NotBlank(message = "Name must be specified")
    private String name;
}
