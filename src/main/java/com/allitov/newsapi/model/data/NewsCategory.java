package com.allitov.newsapi.model.data;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldNameConstants
@Entity
@Table(name = "news_categories")
public class NewsCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name")
    private String name;
}
