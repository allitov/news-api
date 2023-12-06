package com.allitov.newsapi.aop;

import com.allitov.newsapi.exception.IllegalDataAccessException;
import com.allitov.newsapi.model.service.CommentService;
import com.allitov.newsapi.model.service.NewsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.text.MessageFormat;
import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.profiles", name = "active", havingValue = "default")
@Slf4j
public class ChangingAspect {

    private final NewsService newsService;

    private final CommentService commentService;

    @Before("@annotation(Changeable)")
    public void canChange() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String requestURI = request.getRequestURI();
        var pathVariables = (Map<String, String>) request.getAttribute(
                HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE
        );

        Long userId = Long.parseLong(request.getParameter("userId"));
        Long dataId = Long.parseLong(pathVariables.get("id"));

        if (requestURI.startsWith("/api/news/")
                && !newsService.findById(dataId).getAuthor().getId().equals(userId)) {
            throw new IllegalDataAccessException(MessageFormat.format(
                    "User with id = {0} cannot change requested news", userId));
        } else if (requestURI.startsWith("/api/comment/")
                && !commentService.findById(dataId).getAuthor().getId().equals(userId)) {
            throw new IllegalDataAccessException(MessageFormat.format(
                    "User with id = {0} cannot change requested comment", userId));
        }
    }
}
