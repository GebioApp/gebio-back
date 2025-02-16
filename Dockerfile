FROM eclipse-temurin:21-alpine

WORKDIR /app

COPY bootstrap/target/bootstrap-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]