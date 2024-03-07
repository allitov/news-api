package com.allitov.newsapi.configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "Basic authorisation",
        scheme = "basic"
)
public class OpenApiConfiguration {

    @Bean
    public OpenAPI openApiDescription() {
        Server localhostServer = new Server();
        localhostServer.setUrl("http://localhost:8080");
        localhostServer.setDescription("Local env");

        Info info = new Info()
                .title("News API")
                .version("2.0")
                .description("API for news services");

        return new OpenAPI().info(info).servers(List.of(localhostServer));
    }
}
