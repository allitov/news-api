package com.allitov.newsapi.aop;

import com.allitov.newsapi.exception.ExceptionMessage;
import com.allitov.newsapi.exception.IllegalDataAccessException;
import com.allitov.newsapi.model.data.RoleType;
import com.allitov.newsapi.model.service.CommentService;
import com.allitov.newsapi.model.service.NewsService;
import com.allitov.newsapi.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class SecurityAspect {

    private final NewsService newsService;

    private final CommentService commentService;

    @Pointcut(
            value = "execution(* com.allitov.newsapi.web.controller.v2.UserController.*ById(..)) " +
                    "&& args(userId, userDetails, ..)",
            argNames = "userId,userDetails"
    )
    public void userControllerByIdMethodsPointcut(Long userId, UserDetailsImpl userDetails) {}

    @Pointcut(
            value = "execution(* com.allitov.newsapi.web.controller.v2.NewsController.updateById(..)) " +
                    "&& args(newsId, userDetails, ..)",
            argNames = "newsId,userDetails")
    public void newsControllerUpdateByIdMethodPointcut(Long newsId, UserDetailsImpl userDetails) {}

    @Pointcut(
            value = "execution(* com.allitov.newsapi.web.controller.v2.NewsController.deleteById(..)) " +
                    "&& args(newsId, userDetails, ..)",
            argNames = "newsId,userDetails")
    public void newsControllerDeleteByIdMethodPointcut(Long newsId, UserDetailsImpl userDetails) {}

    @Pointcut(
            value = "execution(* com.allitov.newsapi.web.controller.v2.CommentController.updateById(..)) " +
                    "&& args(commentId, userDetails, ..)",
            argNames = "commentId,userDetails")
    public void commentControllerUpdateByIdMethodPointcut(Long commentId, UserDetailsImpl userDetails) {}

    @Pointcut(
            value = "execution(* com.allitov.newsapi.web.controller.v2.CommentController.deleteById(..)) " +
                    "&& args(commentId, userDetails, ..)",
            argNames = "commentId,userDetails")
    public void commentControllerDeleteByIdMethodPointcut(Long commentId, UserDetailsImpl userDetails) {}

    @Before(value = "userControllerByIdMethodsPointcut(userId, userDetails)", argNames = "userId,userDetails")
    public void userControllerByIdMethodsAdvice(Long userId, UserDetailsImpl userDetails) {
        if (userDetails.getAuthorities().size() > 1 || !userDetails.getUser().getRoles().contains(RoleType.USER)) {
            return;
        }

        if (!userDetails.getId().equals(userId)) {
            throw new IllegalDataAccessException(String.format(
                    ExceptionMessage.USER_DATA_ILLEGAL_ACCESS, userDetails.getId(), userId));
        }
    }

    @Before(value = "newsControllerUpdateByIdMethodPointcut(newsId, userDetails)", argNames = "newsId,userDetails")
    public void newsControllerUpdateByIdMethodAdvice(Long newsId, UserDetailsImpl userDetails) {
        Long authorId = newsService.findById(newsId).getAuthor().getId();
        if (!userDetails.getId().equals(authorId)) {
            throw new IllegalDataAccessException(String.format(
                    ExceptionMessage.NEWS_DATA_ILLEGAL_ACCESS, userDetails.getId(), newsId));
        }
    }

    @Before(value = "newsControllerDeleteByIdMethodPointcut(newsId, userDetails)", argNames = "newsId,userDetails")
    public void newsControllerDeleteByIdMethodAdvice(Long newsId, UserDetailsImpl userDetails) {
        if (userDetails.getAuthorities().size() > 1 || !userDetails.getUser().getRoles().contains(RoleType.USER)) {
            return;
        }

        Long authorId = newsService.findById(newsId).getAuthor().getId();
        if (!userDetails.getId().equals(authorId)) {
            throw new IllegalDataAccessException(String.format(
                    ExceptionMessage.NEWS_DATA_ILLEGAL_ACCESS, userDetails.getId(), newsId));
        }
    }

    @Before(value = "commentControllerUpdateByIdMethodPointcut(commentId, userDetails)", argNames = "commentId,userDetails")
    public void commentControllerUpdateByIdMethodAdvice(Long commentId, UserDetailsImpl userDetails) {
        Long authorId = commentService.findById(commentId).getAuthor().getId();
        if (!userDetails.getId().equals(authorId)) {
            throw new IllegalDataAccessException(String.format(
                    ExceptionMessage.COMMENT_DATA_ILLEGAL_ACCESS, userDetails.getId(), commentId));
        }
    }

    @Before(value = "commentControllerDeleteByIdMethodPointcut(commentId, userDetails)", argNames = "commentId,userDetails")
    public void commentControllerDeleteByIdMethodAdvice(Long commentId, UserDetailsImpl userDetails) {
        if (userDetails.getAuthorities().size() > 1 || !userDetails.getUser().getRoles().contains(RoleType.USER)) {
            return;
        }

        Long authorId = commentService.findById(commentId).getAuthor().getId();
        if (!userDetails.getId().equals(authorId)) {
            throw new IllegalDataAccessException(String.format(
                    ExceptionMessage.COMMENT_DATA_ILLEGAL_ACCESS, userDetails.getId(), commentId));
        }
    }
}
