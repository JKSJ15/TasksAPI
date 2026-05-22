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

## Rule-Based AI 🧠

The project includes an intelligent risk analysis engine capable of evaluating tasks automatically based on:

* remaining days until deadline
* task priority
* task status

### Example endpoint

```http id="l4c4xe"
GET /tasks/risk
```

### Example response

```json id="ejb7f5"
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

---

# 📂 Project Structure

```text id="ag6m0z"
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
│   ├── LoginResponseDto.java
│   ├── TaskDto.java
│   └── TaskRiskAiDto.java
│
├── entity
│   ├── Priority.java
│   ├── Status.java
│   ├── Task.java
│   └── User.java
│
├── exception
│   ├── BodyExceptions.java
│   ├── InvalidAtributeException.java
│   ├── TaskNotFoundException.java
│   └── UserNotFoundException.java
│
├── mapper
│   └── TaskMapper.java
│
├── repository
│   ├── TaskRepository.java
│   └── UserRepository.java
│
├── service
│   ├── AuthService.java
│   ├── CustomUserDetailsService.java
│   ├── TaskRiskService.java
│   └── TaskService.java
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

```text id="qg5oc1"
TaskControllerTest.java
TaskRepositoryTest.java
TaskServiceTest.java
IntegrationTest.java
```

---

# 🐳 Docker Support

The application is fully containerized using Docker.

Included files:

* Dockerfile
* docker-compose.yml

Run locally with:

```bash id="8mxwlu"
docker-compose up --build
```

---

# 🔐 Authentication

### Example login/register request

```json id="a5u2u6"
{
  "login": "admin",
  "password": "123456"
}
```

Login returns a JWT token used to access protected endpoints.

---

# 📖 Learning Goals

This project was built to improve knowledge in:

* Backend architecture
* REST APIs
* Authentication
* Spring ecosystem
* Docker
* Testing
* Clean code
* Rule-Based AI systems
* Deploy system

---

# Author

Developed by Jakson José.
