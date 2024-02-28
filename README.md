# News API
**API for news portals and services.**

The application provides the ability to save and edit news and comments on them,
storing user data, as well as creating news categories.

## Running locally
### Clone the repository and move to the working directory
```shell
git clone https://github.com/allitov/news-api.git
cd news-api
```

### Run the application in default mode
```shell
docker-compose --file ./docker/docker-compose-default.yml up -d
```

##### Stop the application
```shell
docker-compose --project-name="news-api" down
```

#### Run the application in demonstration mode (contains entries in the database)
```shell
docker-compose --file ./docker/docker-compose-demo.yml up -d
```

##### Stop the application
```shell
docker-compose --project-name="news-api-demo" down
```

### Run application environment only
```shell
docker-compose --file ./docker/docker-compose-env.yml up -d
```
#### Stop the environment
```shell
docker-compose --project-name="news-api-env" down
```

## Documentation
To familiarize yourself with the application's API and see example queries, 
you can refer to the [interactive Swagger documentation](http://localhost:8080/swagger-ui/index.html) 
(available only after launching the application).