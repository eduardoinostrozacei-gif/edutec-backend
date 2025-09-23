# Imagen base de Java 21
FROM eclipse-temurin:21-jdk

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia pom.xml y descarga dependencias
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

RUN ./mvnw dependency:go-offline

# Copia el resto del código
COPY src src

# Construye el jar (sin tests)
RUN ./mvnw clean package -DskipTests

# Arranca la app
CMD ["java", "-jar", "target/edutec-backend-0.0.1-SNAPSHOT.jar"]
