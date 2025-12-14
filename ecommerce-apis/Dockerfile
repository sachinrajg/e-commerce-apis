FROM eclipse-temurin:17-jdk-focal AS build
WORKDIR /app

COPY pom.xml .
RUN apt-get update && apt-get install -y maven && mvn dependency:go-offline 
COPY src ./src

RUN mvn package -DskipTests

FROM eclipse-temurin:17-jre-focal

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]