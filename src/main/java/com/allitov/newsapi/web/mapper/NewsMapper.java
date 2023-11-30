package com.allitov.newsapi.web.mapper;

import com.allitov.newsapi.model.data.News;
import com.allitov.newsapi.web.dto.request.news.NewsRequest;
import com.allitov.newsapi.web.dto.response.news.NewsListResponse;
import com.allitov.newsapi.web.dto.response.news.NewsResponse;
import com.allitov.newsapi.web.dto.response.news.NewsWithCommentsCount;
import com.allitov.newsapi.web.mapper.delegate.NewsMapperDelegate;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@DecoratedWith(NewsMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {CommentMapper.class})
public interface NewsMapper {

    News requestToNews(NewsRequest request);

    @Mapping(source = "newsId", target = "id")
    News requestToNews(Long newsId, NewsRequest request);

    NewsResponse newsToResponse(News news);

    NewsWithCommentsCount newsToNewsWithCommentsCount(News news);

    default NewsListResponse newsListToNewsListResponse(List<News> news) {
        NewsListResponse response = new NewsListResponse();
        response.setNews(news.stream().map(this::newsToNewsWithCommentsCount).collect(Collectors.toList()));

        return response;
    }
}
