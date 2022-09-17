FROM gradle:7-jdk17 AS builder

COPY --chown=gradle:gradle . /app/src
WORKDIR /app/src
RUN ./gradlew bootJar

# =============================================================================
FROM eclipse-temurin:17-jre AS RUNNER

WORKDIR /app
COPY --from=builder /app/src/build/libs/message-wall-ktor.jar /app/app.jar

CMD ["java","-jar","/app/app.jar"]
