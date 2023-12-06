package com.allitov.newsapi.web.mapper;

import com.allitov.newsapi.model.data.NewsCategory;
import com.allitov.newsapi.web.dto.request.newscategory.NewsCategoryRequest;
import com.allitov.newsapi.web.dto.response.newscategory.NewsCategoryListResponse;
import com.allitov.newsapi.web.dto.response.newscategory.NewsCategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NewsCategoryMapper {

    NewsCategory requestToNewsCategory(NewsCategoryRequest request);

    default NewsCategory requestToNewsCategory(Long categoryId, NewsCategoryRequest request) {
        NewsCategory category = requestToNewsCategory(request);
        category.setId(categoryId);

        return category;
    }

    NewsCategoryResponse newsCategoryToResponse(NewsCategory category);

    List<NewsCategoryResponse> newsCategoryListToResponseList(List<NewsCategory> categories);

    default NewsCategoryListResponse newsCategoryListToNewsCategoryListResponse(List<NewsCategory> categories) {
        NewsCategoryListResponse response = new NewsCategoryListResponse();
        response.setNewsCategories(newsCategoryListToResponseList(categories));

        return response;
    }
}
