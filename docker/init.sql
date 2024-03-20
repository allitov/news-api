CREATE SCHEMA IF NOT EXISTS news_schema;

CREATE TABLE IF NOT EXISTS news_schema.users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(256) NOT NULL,
    password VARCHAR(256) NOT NULL,
    registration_date TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS news_schema.news_categories (
    id BIGSERIAL PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS news_schema.news (
    id BIGSERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    last_update TIMESTAMP NOT NULL,
    author_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    FOREIGN KEY (author_id) REFERENCES news_schema.users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES news_schema.news_categories(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS news_schema.comments (
    id BIGSERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    last_update TIMESTAMP NOT NULL,
    news_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    FOREIGN KEY (news_id) REFERENCES news_schema.news(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES news_schema.users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS news_schema.user_roles (
    user_id BIGINT NOT NULL,
    roles VARCHAR(20) NOT NULL,
    CHECK (((roles)::text = ANY ((ARRAY['USER'::character varying, 'ADMIN'::character varying, 'MODERATOR'::character varying])::text[]))),
    PRIMARY KEY (user_id, roles),
    FOREIGN KEY (user_id) REFERENCES news_schema.users(id) ON DELETE CASCADE
);