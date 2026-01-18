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

Here is the full-complete Markdown code for your README.md. It includes everything we have built: from the Architecture and Security model to the detailed Setup and API instructions.Markdown# 🚀 E-Commerce Enterprise Backend System

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
🚀 Key FeaturesJWT Authentication: Secure stateless authentication with token-based access.Granular Authorization: Permission-based security using @PreAuthorize and hasAuthority.Global Exception Handler: Unified error response format (ApiResponse) for all exceptions.Data Seeding: Automatic initialization of Roles, Permissions, and default Admin accounts on startup.Complex Validation:Strict Phone Number patterns (^0\d{9}$).Custom @DobConstraint for age requirements (minimum 16 years old).Unified error codes for all validation failures.Audit Logging: Structured logging using @Slf4j for production monitoring.📖 API DocumentationThe project includes an interactive Swagger UI to explore and test the endpoints.🔗 Swagger URL: http://localhost:8990/swagger-ui/index.htmlMain EndpointsMethodEndpointAccess RequirementPOST/api/auth/loginPublicPOST/api/auth/registerPublic (Defaults to ROLE_USER)GET/api/users/my-infoAuthenticatedGET/api/usershasAuthority('MANAGE_USERS')POST/api/suppliershasAuthority('CREATE_SUPPLIER')⚙️ Setup & Installation1. PrerequisitesJDK 21 or higherMaven 3.xMySQL 8.x2. Database ConfigurationCreate a schema named ecommerce_db in your MySQL server. Update src/main/resources/application.properties:Propertiesspring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=your_mysql_user
spring.datasource.password=your_mysql_password

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
3. Build and RunExecute the following commands in the project root:Bash# Clean and install dependencies
mvn clean install

# Run the application
mvn spring-boot:run
4. Postman TestingRegister a new account via POST /api/auth/register.Login via POST /api/auth/login to receive your JWT Token.Add the token to the Authorization Header as a Bearer Token for protected requests.
