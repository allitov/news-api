package com.allitov.newsapi.web.mapper.delegate;

import com.allitov.newsapi.model.data.News;
import com.allitov.newsapi.model.service.NewsCategoryService;
import com.allitov.newsapi.model.service.UserService;
import com.allitov.newsapi.web.dto.request.news.NewsRequest;
import com.allitov.newsapi.web.dto.response.news.NewsResponse;
import com.allitov.newsapi.web.dto.response.news.NewsWithCommentsCount;
import com.allitov.newsapi.web.mapper.CommentMapper;
import com.allitov.newsapi.web.mapper.NewsMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class NewsMapperDelegate implements NewsMapper {

    @Autowired
    private UserService userService;

    @Autowired
    private NewsCategoryService newsCategoryService;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public News requestToNews(NewsRequest request) {
        return News.builder()
                .content(request.getContent())
                .author(userService.findById(request.getAuthorId()))
                .category(newsCategoryService.findById(request.getCategoryId()))
                .build();
    }

    @Override
    public News requestToNews(Long newsId, NewsRequest request) {
        News news = requestToNews(request);
        news.setId(newsId);

        return news;
    }

    @Override
    public NewsResponse newsToResponse(News news) {
        return NewsResponse.builder()
                .id(news.getId())
                .content(news.getContent())
                .authorId(news.getAuthor().getId())
                .categoryId(news.getCategory().getId())
                .creationDate(news.getCreationDate())
                .lastUpdate(news.getLastUpdate())
                .comments(commentMapper.commentListToResponseList(news.getComments()))
                .build();
    }


    @Override
    public NewsWithCommentsCount newsToNewsWithCommentsCount(News news) {
        return NewsWithCommentsCount.builder()
                .id(news.getId())
                .content(news.getContent())
                .authorId(news.getAuthor().getId())
                .categoryId(news.getCategory().getId())
                .creationDate(news.getCreationDate())
                .lastUpdate(news.getLastUpdate())
                .commentsCount(news.getComments().size())
                .build();
    }
}
