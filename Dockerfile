# =====================================================================
# Dockerfile: la "receta" para empaquetar la app en una imagen Docker.
# Usa 2 etapas (multi-stage) para que la imagen final sea liviana.
# =====================================================================

# ---- Etapa 1: BUILD (compilar y empaquetar el jar) ----
# Imagen que ya trae Maven + Java 21, solo para construir.
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos primero el pom y bajamos las dependencias.
# (si el pom no cambia, Docker reusa esta capa y va más rápido)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiamos el código y empaquetamos el jar.
# Saltamos los tests acá porque ya corren en el pipeline de GitHub Actions.
COPY src ./src
RUN mvn clean package -DskipTests -B

# ---- Etapa 2: RUNTIME (solo correr el jar) ----
# Imagen mínima con solo el Java necesario para ejecutar (sin Maven).
FROM eclipse-temurin:21-jre
WORKDIR /app

# Traemos únicamente el jar que generó la etapa de build.
COPY --from=build /app/target/*.jar app.jar

# Spring Boot escucha en el 8080 por defecto.
EXPOSE 8080

# Comando que arranca la aplicación al levantar el contenedor.
ENTRYPOINT ["java", "-jar", "app.jar"]
