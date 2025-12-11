FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copia el JAR ya compilado
COPY target/api-0.0.1-SNAPSHOT.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]