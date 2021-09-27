FROM azul/zulu-openjdk-alpine:11.0.12
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

EXPOSE 8090

COPY target/StockManagement-*.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
