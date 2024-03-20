package com.allitov.newsapi.model.service.impl;

import com.allitov.newsapi.exception.ExceptionMessage;
import com.allitov.newsapi.model.data.NewsCategory;
import com.allitov.newsapi.model.repository.NewsCategoryRepository;
import com.allitov.newsapi.model.service.NewsCategoryService;
import com.allitov.newsapi.util.BeanUtils;
import com.allitov.newsapi.web.filter.NewsCategoryFilter;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseNewsCategoryService implements NewsCategoryService {

    private final NewsCategoryRepository newsCategoryRepository;

    @Override
    public NewsCategory findById(Long id) {
        return newsCategoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(ExceptionMessage.NEWS_CATEGORY_BY_ID_NOT_FOUND, id))
        );
    }

    @Override
    public List<NewsCategory> filterBy(NewsCategoryFilter filter) {
        return newsCategoryRepository
                .findAll(PageRequest.of(filter.getPageNumber(), filter.getPageSize()))
                .getContent();
    }

    @Override
    public NewsCategory save(NewsCategory newsCategory) {
        return newsCategoryRepository.save(newsCategory);
    }

    @Override
    public NewsCategory update(NewsCategory newsCategory) {
        NewsCategory foundNewsCategory = findById(newsCategory.getId());
        BeanUtils.copyNonNullProperties(newsCategory, foundNewsCategory);

        return newsCategoryRepository.save(foundNewsCategory);
    }

    @Override
    public void deleteById(Long id) {
        newsCategoryRepository.deleteById(id);
    }
}
