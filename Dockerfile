FROM openjdk:17-jdk

LABEL maintainer="2702461713@qq.com"

WORKDIR /app

COPY sai-web/target/sai-web-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
