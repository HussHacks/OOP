# Jobsy Backend - Javalin

Simple and lightweight job application backend built with Javalin.

## Run

```bash
mvn clean package
java -jar target/jobsy-backend-1.0.0.jar
```

Server: `http://localhost:8080`  
H2 Console: `http://localhost:8080/h2-console`

## Tech Stack

- Javalin 5.6.3
- H2 Database
- HikariCP
- Jackson (JSON)
- JDBC (no JPA)

## Architecture

- **JavalinApp.java** - Main app with routes
- **dao/** - Data access objects (JDBC)
- **services/** - Business logic
- **models/** - POJOs
- **config/** - Database config
