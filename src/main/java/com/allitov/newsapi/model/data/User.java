package com.allitov.newsapi.model.data;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@FieldNameConstants
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String name;

    private String email;

    @CreationTimestamp
    private Instant registrationDate;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @Builder.Default
    private List<News> news = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    public void addNews(News news) {
        if (news != null) {
            this.news.add(news);
        }
    }

    public void removeNews(Long newsId) {
        if (newsId != null) {
            news = news.stream().filter(n -> !n.getId().equals(newsId)).collect(Collectors.toList());
        }
    }

    public void addComment(Comment comment) {
        if (comment != null) {
            comments.add(comment);
        }
    }

    public void removeComment(Long commentId) {
        if (commentId != null) {
            comments = comments.stream().filter(c -> !c.getId().equals(commentId)).collect(Collectors.toList());
        }
    }
}
