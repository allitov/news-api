FROM eclipse-temurin:19-jdk AS build
COPY src /build/src
COPY pom.xml /build
COPY mvnw /build
COPY .mvn /build/.mvn
WORKDIR /build
RUN --mount=type=cache,target=/root/.m2 ./mvnw -f pom.xml clean package -Dmaven.test.skip=true

FROM eclipse-temurin:19-jre
ARG JAR_FILE=/build/target/*.jar
COPY --from=build $JAR_FILE /app/app.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "app.jar"]