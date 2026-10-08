# --- Step 1 : BUILD (Building) ----
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app
ENV MAVEN_OPTS="-Djava.net.preferIPv4Stack=true"
COPY . .
RUN mvn clean package -DskipTests


# --- Step 2 : runtime (final product) ----

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar product.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "product.jar" ]






