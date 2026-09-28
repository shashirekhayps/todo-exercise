# Todo application

Java 21 Spring Boot API with an Angular frontend. Data is kept in memory and resets when the API restarts.

## Run

```bash
cd backend
mvn spring-boot:run
```

In another terminal:

```bash
cd frontend
npm install
npm start
```

Open http://localhost:4200. The API runs on port 8085. Run the small test suites with `mvn test` and `npm test -- --watch=false`.
