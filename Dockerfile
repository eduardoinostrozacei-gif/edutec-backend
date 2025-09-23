# ---------- STAGE 1: BUILD ----------
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos el POM y resolvemos dependencias (cachea)
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

# Copiamos el código y compilamos
COPY src src
RUN mvn -B -q clean package -DskipTests

# ---------- STAGE 2: RUNTIME ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copiamos el JAR construido
COPY --from=build /app/target/edutec-backend-0.0.1-SNAPSHOT.jar app.jar

# Puerto (informativo)
EXPOSE 8080

# Render inyecta $PORT; usamos ese puerto
ENV JAVA_OPTS=""
CMD ["bash", "-lc", "java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar app.jar"]
