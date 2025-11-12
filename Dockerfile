# ================================
# Etapa 1: Build con Maven
# ================================
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar archivos de configuración para aprovechar cache de Docker
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Descargar dependencias (se cachea si pom.xml no cambia)
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar aplicación (sin tests para producción)
RUN mvn clean package -DskipTests

# ================================
# Etapa 2: Runtime optimizado
# ================================
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Instalar curl para health checks (opcional)
RUN apk --no-cache add curl

# Crear usuario no-root para seguridad
RUN addgroup -S spring && adduser -S spring -G spring

# Copiar JAR desde etapa de build
COPY --from=build /app/target/*.jar app.jar

# Cambiar ownership del JAR al usuario spring
RUN chown spring:spring app.jar

# Cambiar a usuario no-root
USER spring:spring

# Exponer puerto (debe coincidir con application.properties)
EXPOSE 8080

# Variables de entorno para optimización de JVM
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Health check (opcional pero recomendado)
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/api/health || exit 1

# Punto de entrada optimizado
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]