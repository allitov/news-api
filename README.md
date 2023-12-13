# News API
**API для новостных порталов и сервисов.**

Приложение предоставляет возможности по сохранению и редактированию новостей и комментариев к ним, 
хранению данных пользователей, а также созданию новостных категорий.

## Сборка приложения
### Клонирование репозитория и переход в рабочую директорию
```shell
git clone https://github.com/allitov/news-api.git
cd news-api
```

### Сборка в виде Docker image
```shell
docker build -t news-api .
```

## Запуск приложения
### Запустить приложение с базой данных в Docker
#### Запустить приложение в обычном режиме
```shell
docker compose --file ./docker/docker-compose-default.yml --project-name="news-api" up
```

##### Остановить работу приложения
```shell
docker compose --project-name="news-api" down
```

#### Запустить приложение в демонстрационном режиме (есть записи в базе данных)
```shell
docker compose --file ./docker/docker-compose-demo.yml --project-name="news-api-demo" up
```

##### Остановить работу приложения
```shell
docker compose --project-name="news-api-demo" down
```

### Запустить только базу данных
```shell
docker compose --file ./docker/docker-compose-postgres.yml --project-name="news-api-db" up
```
#### Остановить работу базы данных
```shell
docker compose --project-name="news-api-db" down
```

## Документация
Для ознакомления с API приложения и просмотра примеров запросов можно обратиться к 
[интерактивной документации Swagger](http://localhost:8080/swagger-ui/index.html).