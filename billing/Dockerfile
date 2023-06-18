FROM openjdk:17-oracle
WORKDIR /app
EXPOSE 8081
COPY target/billing-0.0.3-SNAPSHOT.jar .
CMD ["java", "-jar", "billing-0.0.3-SNAPSHOT.jar"]