# Task API 🚀

A complete REST API for task management built with Java and Spring Boot, featuring JWT authentication, Docker support, automated tests, Swagger documentation, and a Rule-Based AI system for intelligent task risk analysis.

🌐 Live API: https://tasksapi-e8kt.onrender.com

⚠️ For the best testing experience, use:
- Postman
- Insomnia

📄 Swagger Documentation: https://tasksapi-e8kt.onrender.com/swagger-ui/index.html

---

# 📌 Features

## Authentication & Security

* JWT Authentication
* JWT Refresh Token authentication
* Spring Security integration
* Protected routes
* Login and register endpoints
* Security filters

## Task Management

* Create tasks
* Update tasks
* Delete tasks
* List tasks with pagination
* Task status management
* Priority control
* Application logging

## Rule-Based 🧠

The project includes an intelligent risk analysis engine capable of evaluating tasks automatically based on:

* remaining days until deadline
* task priority
* task status

### Example endpoint

```http
GET /tasks/risk
```

### Example response

```json
{
  "content": [
    {
      "idTask": 8,
      "daysRemaining": 1,
      "risk": "CRITICAL",
      "title": "walk with the dog"
    }
  ]
}
```

---

# 🌍 Deploy & Documentation

## API Deploy

https://tasksapi-e8kt.onrender.com

## Swagger UI

https://tasksapi-e8kt.onrender.com/swagger-ui/index.html

---

# 🛠 Technologies

* Java
* Spring Boot
* Spring Security
* JWT
* Swagger / OpenAPI
* Maven
* Docker
* JUnit
* MockMvc
* Pageable API
* REST APIs
* Logging
* CI (Continuous Integration)

---

# 🏗 Architecture

The project follows a layered architecture:

Controller → Service → Repository

Using:

* DTO pattern
* Mapper pattern
* Global exception handling
* Stateless JWT authentication
* Separation of concerns

---

# 📂 Project Structure

```text
src
├── configurations
│   ├── JwtService.java
│   ├── OpenApiConfig.java
│   ├── SecurityConfig.java
│   └── SecurityFilter.java
│
├── controller
│   ├── AuthController.java
│   ├── TaskController.java
│   └── TaskRiskController.java
│
├── dto
│   ├── LoginDto.java
│   ├── TaskDto.java
│   ├── TaskRiskAiDto.java
│   ├── TokenRefreshRequestDto.java
│   └── TokenRefreshResponseDto.java
│
├── entity
│   ├── Priority.java
│   ├── RefreshToken.java
│   ├── Status.java
│   ├── Task.java
│   └── User.java
│
├── exception
│   ├── BodyExceptions.java
│   ├── InvalidAtributeException.java
│   ├── InvalidRefreshTokenException.java
│   ├── TaskNotFoundException.java
│   ├── TokenWasExpiredException.java
│   ├── UserNotFoundException.java
│   └── handler
│       └── GlobalExceptionHandler.java
│
├── mapper
│   └── TaskMapper.java
│
├── repository
│   ├── TaskRepository.java
│   ├── TokenRefreshRepository.java
│   └── UserRepository.java
│
├── service
│   ├── AuthService.java
│   ├── CustomUserDetailsService.java
│   ├── TaskRiskService.java
│   ├── TaskService.java
│   ├── TokenRefreshService.java
│   └── UserService.java
│
└── resources
    └── application.properties
```

---

# 🧪 Tests

The project includes:

* Unit Tests
* Repository Tests
* Controller Tests
* Integration Tests

Main test files:

```text
TaskControllerTest.java
TaskRepositoryTest.java
TaskServiceTest.java
AuthIntegrationTest.java
TasksIntegrationTest.java
```

---

# 🐳 Docker Support

The application is fully containerized using Docker.

Included files:

* Dockerfile
* docker-compose.yml

Run locally with:

```bash
docker-compose up --build
```

---

# 🔐 Authentication

### Example login/register request

```json
{
  "login": "admin",
  "password": "123456"
}
```

Login returns:
- a JWT access token used to access protected endpoints
- a JWT refresh token used to generate new access tokens

---

# 📖 Learning Goals

This project was built to improve knowledge in:

* Backend architecture
* REST APIs
* Authentication and authorization
* Spring ecosystem
* Docker
* Automated testing
* Clean code
* Rule-Based systems
* Deployment
* JWT access and refresh tokens

---

# Author

Developed by Jakson José.
