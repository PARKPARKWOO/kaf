FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "java ${JAVA_OPTS} -jar","-Dspring.profiles.active=prod" , "/app.jar"]