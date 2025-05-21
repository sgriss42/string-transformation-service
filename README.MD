# ENV VARS:
## SPRING_DATASOURCE_URL: jdbc:postgresql://localhost:5432/transformation-server-db
## SPRING_DATASOURCE_USERNAME: transformation-db-user
## SPRING_DATASOURCE_PASSWORD: transformation-db-userpassword
# Build:
### mvn clean package
### docker build -t string-transformation-service .
### docker-compose -f infrastructure/docker/docker-compose.yaml up --build
# SWAGGER:
### http://localhost:8080/swagger-ui/index.html#/