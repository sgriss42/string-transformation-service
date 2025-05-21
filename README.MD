# ENV  VARS:
## POSTGRES_DB: transformation-server-db
## POSTGRES_USER: transformation-db-user
## POSTGRES_PASSWORD: transformation-db-userpassword
# Build:
### docker build -t string-transformation-service .
### docker-compose -f infrastructure/docker/docker-compose.yaml up --build
# SWAGGER:
### http://localhost:8080/swagger-ui/index.html#/