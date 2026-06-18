# =====================================================================
# Sirve para empaquetar la app en una imagen Docker.
# Usa 2 etapas (multi-stage) para que la imagen final sea liviana.
# =====================================================================

# ---- Etapa 1: BUILD (compilar y empaquetar el jar) ----
# Imagen que ya trae Maven + Java 21, solo para construir.
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Descragamos dependendicias, sin cambios entonces rehusa.
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Saltamos los tests, ya lo hizo GitHub Actions.
COPY src ./src
RUN mvn clean package -DskipTests -B

# ---- Etapa 2: RUNTIME ----
# Imagen mínima con solo el Java necesario para ejecutar.
FROM eclipse-temurin:21-jre
WORKDIR /app

# Traemos únicamente el jar que generó la etapa de build.
COPY --from=build /app/target/*.jar app.jar

# Spring Boot escucha en el 8080 por defecto.
EXPOSE 8080

# Comando que arranca la aplicación al levantar el contenedor.
ENTRYPOINT ["java", "-jar", "app.jar"]
