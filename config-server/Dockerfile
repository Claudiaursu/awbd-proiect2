FROM openjdk:17-oracle
WORKDIR /app
EXPOSE 8070
COPY target/config-server-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "config-server-0.0.1-SNAPSHOT.jar"]