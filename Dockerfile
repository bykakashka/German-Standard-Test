FROM adoptopenjdk/openjdk11:jre11u-alpine-nightly
COPY ./target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]