# 🚀 Task Management Application

A robust and scalable task management system built with Spring Boot 3.5.0, featuring advanced architectural patterns and modern development practices.

## 🌟 Features

- **Task Management** 📋
  - Create, read, update, and delete tasks
  - Task categorization and prioritization
  - Subtask support
  - Board and column organization

- **Technical Features** ⚡
  - Rate limiting for API protection
  - In-memory caching with Caffeine
  - Comprehensive API documentation with Swagger/OpenAPI
  - Input validation
  - CORS support
  - AOP for cross-cutting concerns and logging
  - DTO pattern for data transfer
  - API versioning
  - Repository pattern implementation
  - Service layer architecture
  - N-tier architecture
  - Builder pattern for object instantiation
  - Comprehensive logging system

## 🛠️ Technology Stack

- Java 17
- Spring Boot 3.5.0
- PostgreSQL
- Maven
- Docker
- Spring Data JPA
- Spring AOP
- Resilience4j
- Caffeine Cache
- Lombok
- SpringDoc OpenAPI

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- Docker and Docker Compose (optional, for containerized deployment)
- PostgreSQL (if running locally)

### 🏗️ Building and Running the Project

#### Local Development

1. Install dependencies and build the project:
```bash
mvn clean install
```
2. Run the application locally:
```bash
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`

#### Docker Deployment

1. Build the Docker image:
```bash
docker build -t taskapp .
```

2. Run the application:
```bash
docker run -p 8080:8080 taskapp
```

### 🐘 Database Setup

The application uses PostgreSQL. You can run it using Docker:

```bash
docker run --name task-postgres \
  -e POSTGRES_DB=task \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=1234 \
  -p 5432:5432 \
  -d postgres
```

### 🔧 Configuration

The application configuration is in `application.yaml`. Key configurations include:

- Database connection
- Rate limiting settings
- Cache configuration
- AOP settings

## 📚 API Documentation

Swagger UI is available at: `http://localhost:8080/swagger-ui.html`

The OpenAPI specification can be accessed at: `http://localhost:8080/v3/api-docs`

## 🛡️ Security & Performance Features

### Rate Limiting
- Task Service: 10 requests/second
- Board Service: 5 requests/second
- Column Service: 8 requests/second
- Subtask Service: 15 requests/second

### Caching
- In-memory caching using Caffeine
- Configurable cache settings

### Logging & AOP
- Aspect-Oriented Programming for cross-cutting concerns
- Comprehensive logging system using SLF4J and Logback
- Logging of:
  - API requests and responses
  - Performance metrics
  - Error tracking
  - Business operations
- Configurable log levels and patterns

### Input Validation
- Comprehensive input validation using Spring Validation

## 🧪 Testing

### API Testing
The application includes comprehensive API testing using:
- OpenApi/SwaggerUI

API testing also means Black Box testing in this context, as it focuses on testing the application's functionality without knowledge of its internal workings.

## 📦 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/example/taskapp/
│   │       ├── config/        # Configuration classes
│   │       ├── controller/    # REST controllers
│   │       ├── dto/          # Data Transfer Objects
│   │       ├── entity/       # JPA entities
│   │       ├── repository/   # Data access layer
│   │       ├── service/      # Business logic
│   │       └── util/         # Utility classes
│   └── resources/
│       └── application.yaml  # Application configuration
└── test/                    # Test classes
```

## 🔄 Development Workflow

1. Clone the repository
2. Set up the development environment
3. Run the database
4. Start the application
5. Access Swagger UI for API testing
6. Make changes and test
7. Build and deploy