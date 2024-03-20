package com.allitov.newsapi.model.repository.specification;

import com.allitov.newsapi.model.data.News;
import com.allitov.newsapi.model.data.NewsCategory;
import com.allitov.newsapi.model.data.User;
import com.allitov.newsapi.web.filter.NewsFilter;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@UtilityClass
public class NewsSpecification {

    public static Specification<News> withFilter(NewsFilter filter, List<Long> ids) {
        return Specification.where(byIdsIn(ids))
                .and(byAuthorName(filter.getAuthor()))
                .and(byNewsCategory(filter.getCategory()));
    }

    private static Specification<News> byIdsIn(List<Long> ids) {
        return (root, query, criteriaBuilder) -> {
            if (ids == null) {
                return null;
            }

            query.orderBy(criteriaBuilder.asc(root.get(News.Fields.id)));

            return criteriaBuilder.in(root.get(News.Fields.id)).value(ids);
        };
    }

    private static Specification<News> byAuthorName(String authorName) {
        return (root, query, criteriaBuilder) -> {
            if (authorName == null) {
                return null;
            }

            return criteriaBuilder.equal(
                    root.get(News.Fields.author).get(User.Fields.username), authorName
            );
        };
    }

    private static Specification<News> byNewsCategory(String newsCategory) {
        return (root, query, criteriaBuilder) -> {
            if (newsCategory == null) {
                return null;
            }

            return criteriaBuilder.equal(
                    root.get(News.Fields.category).get(NewsCategory.Fields.name), newsCategory
            );
        };
    }
}
