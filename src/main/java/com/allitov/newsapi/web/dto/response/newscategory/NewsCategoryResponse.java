package com.allitov.newsapi.web.dto.response.newscategory;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewsCategoryResponse {

    private Long id;

    private String name;
}
