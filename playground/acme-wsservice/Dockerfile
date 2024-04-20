FROM openjdk:21
MAINTAINER eljeko
COPY target/acme-service-1.0.0-SNAPSHOT-runner.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Dquarkus.http.host=0.0.0.0","-jar","/app.jar"]