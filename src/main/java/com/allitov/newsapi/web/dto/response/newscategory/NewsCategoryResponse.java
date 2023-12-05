package com.allitov.newsapi.web.dto.response.newscategory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewsCategoryResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "News category")
    private String name;
}
