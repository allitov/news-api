package com.allitov.newsapi.web.filter;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserFilter {

    private Integer pageSize;

    private Integer pageNumber;
}
