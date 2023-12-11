package com.allitov.newsapi;

import com.allitov.newsapi.testcontainer.PostgresContainerInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(initializers = PostgresContainerInitializer.class)
class NewsApiApplicationTests {

    @Test
    void contextLoads() {
    }

}
