FROM eclipse-temurin:21-jdk

WORKDIR /tasks

EXPOSE 8080

COPY target/tasks.jar tasks.jar

ENTRYPOINT ["java", "-jar", "tasks.jar"]