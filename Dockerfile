FROM openjdk:19-oracle
WORKDIR /app
COPY target/NewsAPI-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]