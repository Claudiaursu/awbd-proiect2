FROM openjdk:17
EXPOSE 8090
ADD internet/target/awbd2.jar awbd-proiect2.jar
ENTRYPOINT ["java", "-jar", "/awbd-proiect2.jar"]