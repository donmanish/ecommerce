# E-Commerce API Documentation

This document describes the APIs for the E-Commerce website. The system supports **user authentication**, **product listing with pagination, sorting, and search**, **order management**, **payment processing**, and **caching using Redis**. The backend is deployed on **AWS EC2** with database on **AWS RDS**.

---

## Setup Instructions

Follow these steps after cloning the repository:

### 1. Clone the repository

    git clone https://github.com/your-username/ecommerce-backend.git
    cd ecommerce-backend

### 2. Configure Environment Variables

    Create a .env file or use application.properties / application.yml for Spring Boot:

### 3. Build the project

    Use Maven or Gradle:
    
    # Using Maven
    mvn clean install
    
    # Using Gradle
    gradle build

### 4. Run the application
    # Using Maven
    mvn spring-boot:run
    
    # Or run the jar file
    java -jar target/ecommerce-backend-0.0.1-SNAPSHOT.jar

### 5. http://localhost:8080/
     # if you can see swagger

# API Documentation

## Authentication APIs

Endpoints for user authentication (register, login, logout).

| Method | Endpoint | Description |
|--------|---------|-------------|
| POST | `/auth/register` | Register a new user |
| POST | `/auth/login` | Login a user |
| POST | `/auth/logout/{userId}` | Logout a user by their ID |

---

## Order APIs

Endpoints for managing orders.

| Method | Endpoint | Description |
|--------|---------|-------------|
| GET | `/orders` | List all orders |
| GET | `/orders/{id}` | Get order by ID |
| POST | `/orders` | Create a new order |

---

## Payment APIs

Endpoints for handling order payments.

| Method | Endpoint | Description |
|--------|---------|-------------|
| POST | `/payments/create-intent` | Create a payment intent for an order |

---

## Product APIs

Endpoints for managing products.

| Method | Endpoint | Description |
|--------|---------|-------------|
| GET | `/products` | List products with pagination, sorting (`asc`, `desc`), and search. **Note:** When searching, remove `page` and `size` query parameters. |
| GET | `/products/{id}` | Get a product by ID |
| POST | `/products` | Create a new product |
| PUT | `/products/{id}` | Update a product by ID |
| DELETE | `/products/{id}` | Delete a product by ID |

---

## Category APIs

Endpoints for managing product categories.

| Method | Endpoint | Description |
|--------|---------|-------------|
| GET | `/categories` | List all categories |
| GET | `/categories/{title}` | Get category details by title |

---

## Health Check API

| Method | Endpoint | Description |
|--------|---------|-------------|
| GET | `/health` | Check server health/status |

---


# Caching (Redis)

    Frequently accessed endpoints like /login, /products and /categories are cached in Redis.
    Cache invalidation occurs on product/category creation, update, or deletion.
    Improves performance by reducing database queries (AWS RDS).

# Deployment

    Backend server: AWS EC2
    Database: AWS RDS (PostgreSQL/MySQL)
    Caching: Redis (AWS ElastiCache or local Redis on EC2)
    Security: JWT-based authentication
    Environment variables are configured for DB, JWT secret, and Redis.

## Notes

    1. All endpoints that require authentication should include a valid JWT token in the `Authorization` header:  
