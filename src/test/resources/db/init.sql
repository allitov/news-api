DROP TABLE IF EXISTS news_schema.comments;
DROP TABLE IF EXISTS news_schema.news;
DROP TABLE IF EXISTS news_schema.news_categories;
DROP TABLE IF EXISTS news_schema.user_roles;
DROP TABLE IF EXISTS news_schema.users;

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

insert into news_schema.users (username, email, password, registration_date) values ('Garek Simper', 'gsimper0@dropbox.com', '$2a$04$5ZDXOb3IHiy5w4laMT7vh.umbLCzxNkT3xJMAm5zo0oi0idceAtca', '2023-06-30 15:03:20');
insert into news_schema.users (username, email, password, registration_date) values ('Nial Lodemann', 'nlodemann1@state.gov', '$2a$04$AzvbtdT2NT0ZrdlDakJgduIstaoKmgClLWWAplob4PHHzy9RLDRUi', '2023-01-28 03:00:27');
insert into news_schema.users (username, email, password, registration_date) values ('Emmey Crossland', 'ecrossland2@canalblog.com', '$2a$04$rqJC3hO/U8pubX4s9KypqejEbhMSlnxp16jtJsVr1soQT8hrSC5dK', '2023-06-08 19:53:03');
insert into news_schema.users (username, email, password, registration_date) values ('Chas Sapshed', 'csapshed3@noaa.gov', '$2a$04$EylmMDBQWbcA1Vi0q8msQOog7ROi70WiKDhno.jsHvvErI.0vZU.O', '2022-12-13 01:10:09');
insert into news_schema.users (username, email, password, registration_date) values ('Fina Sugden', 'fsugden4@nymag.com', '$2a$04$f18HajB3iSvy0dXqS403UuULcxm624fmwMzMSQ8qaJl3xRRcUw4Yu', '2023-04-14 06:02:56');

insert into news_schema.news_categories (category_name) values ('CDL');
insert into news_schema.news_categories (category_name) values ('PK/PD');
insert into news_schema.news_categories (category_name) values ('FX Spot');
insert into news_schema.news_categories (category_name) values ('Overseas Sourcing');
insert into news_schema.news_categories (category_name) values ('SQF');

insert into news_schema.news (content, creation_date, last_update, author_id, category_id) values ('Sed sagittis. Nam congue, risus semper porta volutpat, quam pede lobortis ligula, sit amet eleifend pede libero quis orci. Nullam molestie nibh in lectus. Pellentesque at nulla. Suspendisse potenti. Cras in purus eu magna vulputate luctus.', '2023-10-03 03:52:13', '2023-04-10 11:34:31', 4, 2);
insert into news_schema.news (content, creation_date, last_update, author_id, category_id) values ('Proin eu mi. Nulla ac enim. In tempor, turpis nec euismod scelerisque, quam turpis adipiscing lorem, vitae mattis nibh ligula nec sem. Duis aliquam convallis nunc. Proin at turpis a pede posuere nonummy. Integer non velit.', '2023-02-23 18:11:51', '2023-01-17 19:50:37', 4, 4);
insert into news_schema.news (content, creation_date, last_update, author_id, category_id) values ('Fusce consequat. Nulla nisl. Nunc nisl. Duis bibendum, felis sed interdum venenatis, turpis enim blandit mi, in porttitor pede justo eu massa. Donec dapibus. Duis at velit eu est congue elementum. In hac habitasse platea dictumst. Morbi vestibulum, velit id pretium iaculis, diam erat fermentum justo, nec condimentum neque sapien placerat ante. Nulla justo.', '2023-02-08 04:35:43', '2023-06-07 19:19:06', 3, 2);
insert into news_schema.news (content, creation_date, last_update, author_id, category_id) values ('Quisque id justo sit amet sapien dignissim vestibulum. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Nulla dapibus dolor vel est. Donec odio justo, sollicitudin ut, suscipit a, feugiat et, eros. Vestibulum ac est lacinia nisi venenatis tristique. Fusce congue, diam id ornare imperdiet, sapien urna pretium nisl, ut volutpat sapien arcu sed augue. Aliquam erat volutpat. In congue. Etiam justo. Etiam pretium iaculis justo.', '2023-03-19 03:22:59', '2022-12-22 04:09:45', 4, 1);
insert into news_schema.news (content, creation_date, last_update, author_id, category_id) values ('Phasellus in felis. Donec semper sapien a libero. Nam dui.', '2023-02-06 08:13:10', '2023-07-29 04:21:55', 5, 4);

insert into news_schema.comments (content, creation_date, last_update, news_id, author_id) values ('Integer tincidunt ante vel ipsum. Praesent blandit lacinia erat. Vestibulum sed magna at nunc commodo placerat.', '2023-05-29 21:54:09', '2022-11-30 15:59:01', 4, 3);
insert into news_schema.comments (content, creation_date, last_update, news_id, author_id) values ('Quisque id justo sit amet sapien dignissim vestibulum. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Nulla dapibus dolor vel est. Donec odio justo, sollicitudin ut, suscipit a, feugiat et, eros. Vestibulum ac est lacinia nisi venenatis tristique. Fusce congue, diam id ornare imperdiet, sapien urna pretium nisl, ut volutpat sapien arcu sed augue. Aliquam erat volutpat.', '2023-01-08 17:52:29', '2023-09-21 01:06:49', 5, 1);
insert into news_schema.comments (content, creation_date, last_update, news_id, author_id) values ('Maecenas ut massa quis augue luctus tincidunt. Nulla mollis molestie lorem. Quisque ut erat. Curabitur gravida nisi at nibh. In hac habitasse platea dictumst. Aliquam augue quam, sollicitudin vitae, consectetuer eget, rutrum at, lorem.', '2023-01-16 18:16:05', '2023-10-02 17:34:20', 1, 3);
insert into news_schema.comments (content, creation_date, last_update, news_id, author_id) values ('In quis justo. Maecenas rhoncus aliquam lacus. Morbi quis tortor id nulla ultrices aliquet. Maecenas leo odio, condimentum id, luctus nec, molestie sed, justo. Pellentesque viverra pede ac diam. Cras pellentesque volutpat dui. Maecenas tristique, est et tempus semper, est quam pharetra magna, ac consequat metus sapien ut nunc. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Mauris viverra diam vitae quam. Suspendisse potenti.', '2023-10-15 11:21:35', '2023-02-09 19:41:29', 5, 4);
insert into news_schema.comments (content, creation_date, last_update, news_id, author_id) values ('Aliquam quis turpis eget elit sodales scelerisque. Mauris sit amet eros. Suspendisse accumsan tortor quis turpis. Sed ante. Vivamus tortor. Duis mattis egestas metus. Aenean fermentum. Donec ut mauris eget massa tempor convallis. Nulla neque libero, convallis eget, eleifend luctus, ultricies eu, nibh.', '2023-08-12 07:31:52', '2023-06-03 21:50:53', 1, 1);

insert into news_schema.user_roles (user_id, roles) values (1, 'ADMIN');
insert into news_schema.user_roles (user_id, roles) values (1, 'MODERATOR');
insert into news_schema.user_roles (user_id, roles) values (1, 'USER');
insert into news_schema.user_roles (user_id, roles) values (2, 'MODERATOR');
insert into news_schema.user_roles (user_id, roles) values (2, 'USER');
insert into news_schema.user_roles (user_id, roles) values (3, 'USER');
insert into news_schema.user_roles (user_id, roles) values (4, 'USER');
insert into news_schema.user_roles (user_id, roles) values (5, 'USER');