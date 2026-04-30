# 🍽️ Restaurant Booking API

REST API for restaurant table reservation management, built with Java 21, Spring Boot 3 and MongoDB.

## Tech Stack

- **Java 21** + **Spring Boot 3.3**
- **MongoDB** — document database
- **Spring Security** + **JWT** — stateless authentication
- **Maven** — dependency management
- **Swagger / OpenAPI** — interactive API documentation
- **Lombok** — boilerplate reduction
- **Docker** — MongoDB containerization

## Features

- User registration and login with JWT authentication
- Table management (create, update, delete)
- Reservation system with overlap validation
- Role-based access control (USER / ADMIN)
- Global exception handling with descriptive error responses
- Interactive API documentation with Swagger UI

## Getting Started

### Prerequisites

- Java 21
- Docker (to run MongoDB)

### Installation

1. Clone the repository
```bash
   git clone https://github.com/tu-usuario/restaurant-booking-api.git
   cd restaurant-booking-api
```

2. Start MongoDB with Docker
```bash
   docker run -d -p 27017:27017 --name mongodb mongo
```

3. Set up configuration
```bash
   cp src/main/resources/application-example.properties src/main/resources/application.properties
```
   Edit `application.properties` and set your `jwt.secret` (minimum 32 characters).

4. Run the application
```bash
   ./mvnw spring-boot:run
```

5. Open Swagger UI
```
   http://localhost:8080/swagger-ui/index.html
```

## API Endpoints

### Auth
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/auth/registro` | Register new user | Public |
| POST | `/api/auth/login` | Login and get JWT token | Public |

### Tables
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/api/mesas` | Get all tables | Required |
| GET | `/api/mesas/disponibles` | Get available tables | Required |
| GET | `/api/mesas/{id}` | Get table by ID | Required |
| POST | `/api/mesas` | Create table | Required |
| PUT | `/api/mesas/{id}` | Update table | Required |
| DELETE | `/api/mesas/{id}` | Delete table | Required |

### Reservations
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/reservas` | Create reservation | Required |
| GET | `/api/reservas` | Get all reservations | Required |
| GET | `/api/reservas/mis-reservas` | Get my reservations | Required |
| PATCH | `/api/reservas/{id}/cancelar` | Cancel reservation | Required |

## Authentication

All protected endpoints require a Bearer token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

Get your token by calling `/api/auth/login`. In Swagger UI, click **Authorize** and paste the token.

## Project Structure

```
src/main/java/com/tuapp/restaurante/
├── config/          # Security and Swagger configuration
├── controller/      # REST controllers
├── dto/             # Data Transfer Objects
├── exception/       # Global exception handling
├── model/           # MongoDB documents
├── repository/      # Data access layer
├── security/        # JWT filter and utilities
└── service/         # Business logic
```
