# 🚀 E-Commerce Enterprise Backend System

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![Java](https://img.shields.io/badge/Java-21-blue)
![Security](https://img.shields.io/badge/Spring%20Security-JWT-orange)
![MySQL](https://img.shields.io/badge/Database-MySQL-blue)

A robust, production-grade backend architecture for E-commerce platforms, designed with a focus on **Fine-Grained Access Control (RBAC)**, security, and scalability.

[Image of role based access control model]

---

## 🛠 Tech Stack

* **Framework:** Spring Boot 3.x (Java 21)
* **Security:** Spring Security & JWT (JSON Web Token)
* **Database:** MySQL 8.0
* **Persistence:** Spring Data JPA (Hibernate 6)
* **Mapping:** MapStruct (Compile-time mapping for high performance)
* **Documentation:** OpenAPI 3 / Swagger UI
* **Validation:** Hibernate Validator (JSR-380) & Custom Annotations

---

## 🔐 Advanced Security & RBAC Model

The system implements a sophisticated **Role-Based Access Control (RBAC)** model. Instead of checking simple roles, the system validates specific **Authorities (Permissions)** for every sensitive action.

### Role Hierarchy
| Role | Description |
| :--- | :--- |
| **ADMIN** | Full system access, User management, and Data deletion. |
| **STAFF** | Catalog management, Supplier management (Create/Update). |
| **USER** | End-customer access, profile management, and viewing products. |

### Permissions System
Every API endpoint is protected by specific authorities (e.g., `MANAGE_USERS`, `CREATE_SUPPLIER`). This allows for high flexibility—changing a Staff member's permissions is done via Database without changing code.

---

## 🏗 Project Architecture

The project follows a clean, layered architecture to ensure separation of concerns and maintainability:

```text
src/main/java/com/fpt/ecommerce/
├── config/             # Configuration (Security, Swagger, DataSeeder)
├── constant/           # Application constants & Permission definitions
├── controller/         # REST Controllers (Web Layer)
├── dto/                # Data Transfer Objects (Request/Response)
├── entity/             # Database Models (JPA Entities)
├── exception/          # Centralized Exception Handling & ErrorCodes
├── mapper/             # MapStruct Interfaces for Entity-DTO conversion
├── repository/         # Data Access Layer (Spring Data JPA)
├── security/           # JWT Logic, Filters, and UserDetailsService
├── service/            # Business Logic Layer
└── validator/          # Custom Constraints & Validation Logic
