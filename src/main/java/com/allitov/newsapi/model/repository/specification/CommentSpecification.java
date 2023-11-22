package com.allitov.newsapi.model.repository.specification;

import com.allitov.newsapi.model.data.Comment;
import com.allitov.newsapi.model.data.News;
import com.allitov.newsapi.web.filter.CommentFilter;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class CommentSpecification {

    public static Specification<Comment> withFilter(CommentFilter filter) {
        return Specification.where(byNewsId(filter.getNewsId()));
    }

    private static Specification<Comment> byNewsId(Long newsId) {
        return (root, query, criteriaBuilder) -> {
            if (newsId == null) {
                return null;
            }

            return criteriaBuilder.equal(
                    root.get(Comment.Fields.news).get(News.Fields.id), newsId
            );
        };
    }
}
