FROM maven:3.8.4-openjdk-11
MAINTAINER bhimsur
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package
RUN cp target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]