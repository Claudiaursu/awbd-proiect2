FROM openjdk:17-oracle
WORKDIR /app
EXPOSE 8761
COPY target/eureka-server-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "eureka-server-0.0.1-SNAPSHOT.jar"]