FROM eclipse-temurin:17-jdk-alpine
COPY ./build/libs/evc-0.0.1-SNAPSHOT.jar project.jar
ENTRYPOINT ["java", "-jar", "project.jar"]
