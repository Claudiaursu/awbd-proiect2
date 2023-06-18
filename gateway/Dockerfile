FROM openjdk:17-oracle
WORKDIR /app
EXPOSE 8071
COPY target/gateway-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "gateway-0.0.1-SNAPSHOT.jar"]