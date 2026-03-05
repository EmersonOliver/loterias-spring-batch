FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/megasena-batch-*.jar app.jar
COPY resultados/*.csv resultados/

ENTRYPOINT ["java", "-jar", "app.jar"]
