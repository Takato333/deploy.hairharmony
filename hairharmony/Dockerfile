FROM maven:3-openjdk-17 AS build
WORKDIR /src

COPY . .
RUN mvn clean package -DskipTests


# Run stage

FROM openjdk:17-jdk-slim
WORKDIR /src

COPY --from=build /hairharmony/target/hairharmony-0.0.1-SNAPSHOT.war hairharmony.war
EXPOSE 8080 

ENTRYPOINT ["java","-jar","hairharmony.war"]

