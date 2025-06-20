# Build Stage
FROM gradle:8.4-jdk21 AS builder

# 캐싱
WORKDIR /app
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle
RUN gradle build -x test || return 0  # 초기 캐시용, 실패해도 넘어감

# 전체 소스 복사 후 실제 build
COPY . .
RUN gradle bootJar --no-daemon

# Run Stage
FROM eclipse-temurin:21-jdk
WORKDIR /app

# copy built jar file
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
