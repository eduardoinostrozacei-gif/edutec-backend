
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app


COPY pom.xml .
RUN mvn -B -q dependency:go-offline


COPY src src
RUN mvn -B -q clean package -DskipTests


FROM eclipse-temurin:21-jre
WORKDIR /app


COPY --from=build /app/target/edutec-backend-0.0.1-SNAPSHOT.jar app.jar


EXPOSE 8080


ENV JAVA_OPTS=""
CMD ["bash", "-lc", "java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar app.jar"]
