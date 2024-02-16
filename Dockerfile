FROM openjdk:17

ARG JAR=build/libs/*.jar

COPY ${JAR} app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]