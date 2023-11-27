package com.allitov.newsapi.web.controller;

import com.allitov.newsapi.model.data.Comment;
import com.allitov.newsapi.model.data.News;
import com.allitov.newsapi.model.data.NewsCategory;
import com.allitov.newsapi.model.data.User;
import com.allitov.newsapi.web.dto.response.user.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    private final Instant time = Instant.parse("1970-01-01T00:00:00Z");

    protected User createUser(Long id, String name, String email,
                              List<News> news, List<Comment> comments) {
        User user = User.builder()
                .id(id)
                .name(name)
                .email(email)
                .registrationDate(time)
                .build();

        if (news != null) {
            news.forEach(n -> {
                user.addNews(n);
                n.setAuthor(user);
            });
        }

        if (comments != null) {
            comments.forEach(c -> {
                user.addComment(c);
                c.setAuthor(user);
            });
        }

        return user;
    }

    protected News createNews(Long id, String content, User author,
                              NewsCategory category, List<Comment> comments) {
        News news = News.builder()
                .id(id)
                .content(content)
                .creationDate(time)
                .lastUpdate(time)
                .author(author)
                .category(category)
                .build();

        if (comments != null) {
            comments.forEach(c -> {
                news.addComment(c);
                c.setNews(news);
            });
        }

        return news;
    }

    protected NewsCategory createNewsCategory(Long id, String name) {
        return NewsCategory.builder()
                .id(id)
                .name(name)
                .build();
    }

    protected Comment createComment(Long id, String content, User author, News news) {
        return Comment.builder()
                .id(id)
                .content(content)
                .author(author)
                .news(news)
                .creationDate(time)
                .lastUpdate(time)
                .build();
    }

    protected UserResponse createUserResponse(Long id, String name, String email) {
        return UserResponse.builder()
                .id(id)
                .name(name)
                .email(email)
                .regDate(time)
                .build();
    }
}
