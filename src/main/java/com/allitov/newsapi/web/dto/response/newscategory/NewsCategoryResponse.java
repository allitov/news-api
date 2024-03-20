package com.allitov.newsapi.web.dto.response.newscategory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsCategoryResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "News category")
    private String name;
}
