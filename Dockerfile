FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY entrypoint.sh entrypoint.sh
ENTRYPOINT ["./entrypoint.sh"]
