# --- build stage ---
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copiamos el Maven Wrapper y resolvemos dependencias
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -B -q dependency:go-offline

# Copiamos el código y construimos
COPY src ./src
RUN ./mvnw -B -q clean package -DskipTests

# --- runtime stage ---
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/edutec-backend-0.0.1-SNAPSHOT.jar app.jar

# Render inyecta PORT; si no viene, usa 8080
ENV JAVA_OPTS=""
EXPOSE 8080
CMD ["bash","-lc","java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar app.jar"]
