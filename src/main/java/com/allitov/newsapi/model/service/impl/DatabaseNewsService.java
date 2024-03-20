package com.allitov.newsapi.model.service.impl;

import com.allitov.newsapi.exception.ExceptionMessage;
import com.allitov.newsapi.model.data.News;
import com.allitov.newsapi.model.repository.NewsRepository;
import com.allitov.newsapi.model.repository.specification.NewsSpecification;
import com.allitov.newsapi.model.service.NewsService;
import com.allitov.newsapi.util.BeanUtils;
import com.allitov.newsapi.web.filter.NewsFilter;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseNewsService implements NewsService {

    private final NewsRepository newsRepository;

    @Override
    public News findById(Long id) {
        return newsRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(ExceptionMessage.NEWS_BY_ID_NOT_FOUND, id)));
    }

    @Override
    public List<News> filterBy(NewsFilter filter) {
        List<Long> ids = newsRepository.getAllIds(
                PageRequest.of(filter.getPageNumber(), filter.getPageSize())).getContent();
        return newsRepository.findAll(
                NewsSpecification.withFilter(filter, ids));
    }

    @Override
    public News save(News news) {
        return newsRepository.save(news);
    }

    @Override
    public News update(News news) {
        News foundNews = findById(news.getId());
        BeanUtils.copyNonNullProperties(news, foundNews);

        return newsRepository.save(foundNews);
    }

    @Override
    public void deleteById(Long id) {
        newsRepository.deleteById(id);
    }
}
