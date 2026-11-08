FROM gradle:9.5-jdk21 AS build
WORKDIR /workspace
COPY . .
RUN gradle :service:installDist --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/service/build/install/service/ /app/
EXPOSE 8080
ENTRYPOINT ["/app/bin/service"]
