FROM maven:3-openjdk-17 AS build
WORKDIR /app

COPY . .
RUN mvn clean package -DskipTests


# Run stage

FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=build /app/target/Drcomputer-0.0.1-SNAPSHOT.war drcomputer.war
EXPOSE 8080 

<<<<<<< HEAD
ENTRYPOINT ["java","-jar","drcomputer.war"]
=======
ENTRYPOINT ["java","-jar","drcomputer.war"]
>>>>>>> 39a206c053633c0ec1e8ca2429ecd5ce25f43c14
