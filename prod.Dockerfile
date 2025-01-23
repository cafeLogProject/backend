FROM openjdk:21-jdk-slim

WORKDIR /app

# COPY만 docker-compose 파일의 위치를 기반으로 작동함
COPY . .

# 개행문자 오류 해결 [unix와 window 시스템 차이]
RUN sed -i 's/\r$//' gradlew

# RUN은 현재 파일을 위치를 기반으로 작동함
RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test --no-daemon

ENV JAR_NAME=cafeLog-0.0.1-SNAPSHOT.jar
RUN mv /app/build/libs/${JAR_NAME} /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

