# ================================
# Etapa 1: Build con Maven
# ================================
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Configurar encoding global
ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8

# Copiar archivos del proyecto
COPY pom.xml .
COPY src ./src

# Compilar aplicación
RUN mvn clean package -DskipTests

# ================================
# Etapa 2: Runtime optimizado
# ================================
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Configurar encoding en runtime
ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8

# Crear usuario no-root para seguridad
RUN addgroup -S spring && adduser -S spring -G spring

# Copiar JAR desde etapa de build
COPY --from=build /app/target/*.jar app.jar

# Cambiar ownership del JAR
RUN chown spring:spring app.jar

# Cambiar a usuario no-root
USER spring:spring

# Exponer puerto
EXPOSE 8080

# Variables de entorno para JVM
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Punto de entrada
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]