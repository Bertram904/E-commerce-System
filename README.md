# 🚀 E-Commerce Enterprise Backend API

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![Java](https://img.shields.io/badge/Java-17%2B-blue)
![Security](https://img.shields.io/badge/Spring%20Security-JWT-orange)
![Database](https://img.shields.io/badge/MySQL-8.0-blue)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI%203-green)

A production-grade E-commerce RESTful API built with Spring Boot, focusing on **concurrency handling**, **financial data integrity**, and **secure architecture**.

The system runs on **Port 8990** and includes advanced features like Pessimistic Locking for inventory and Snapshot Pricing for orders.

---

## 🔥 Key Technical Highlights (Interview Focus)

### 1. 🛡️ Security & Authentication
* **Stateless Architecture:** Implemented **JWT (JSON Web Token)** for secure, stateless authentication.
* **Role-Based Access Control (RBAC):** Fine-grained permissions for `ADMIN`, `STAFF`, and `USER`.
* **Password Security:** BCrypt hashing for password storage.

### 2. ⚡ Concurrency & Inventory (The "Checkout" Problem)
* **Pessimistic Locking:** Utilized `LockModeType.PESSIMISTIC_WRITE` (SELECT FOR UPDATE) in `ProductRepository`. This prevents **Race Conditions** where multiple users buy the last item simultaneously.
* **ACID Transactions:** The Checkout process (`OrderService`) is fully transactional. If any step (Stock deduction, Order creation, Cart clearing) fails, the entire transaction rolls back.

### 3. 🛒 Smart Cart & Pricing Logic
* **Dynamic vs. Snapshot Pricing:**
    * **Cart:** Displays *current* product prices (Dynamic).
    * **Order:** Saves a *snapshot* of the price at the moment of purchase (`OrderDetail`), ensuring historical data accuracy even if the product price changes later.
* **Lazy Cart Initialization:** Automatically creates a cart for new users upon their first "Add to Cart" action to prevent `NullPointerException` or 404 errors.

### 4. 🚀 Performance & Optimization
* **Set vs. List in Hibernate:** Used `Set<Entity>` for `@OneToMany` relationships to avoid `MultipleBagFetchException` and optimize delete operations.
* **Batch Processing:** Implemented `deleteAllInBatch` for clearing the cart, reducing database round-trips significantly.
* **Soft Delete:** Data is never physically deleted (using `@SQLDelete` and `@Where`), preserving audit trails.

---

## 🏗️ Module Overview

| Module | Features |
| :--- | :--- |
| **Authentication** | Login, Register, Token Generation, Current User Context. |
| **Category** | CRUD operations, hierarchical organization. |
| **Product** | CRUD, Stock Management, Soft Delete, Image URL handling. |
| **Cart** | Add Item (with Stock check), Update Quantity, Remove Item, Auto-calculate Totals. |
| **Order** | **Checkout (Atomic Transaction)**, Stock Deduction, Order History. |

---

## 🛠 Tech Stack

* **Framework:** Spring Boot 3.5.9
* **Language:** Java 25 (Compatible with 17+)
* **Database:** MySQL 8.0.43
* **ORM:** Hibernate / Spring Data JPA
* **Mapper:** MapStruct (Compile-time mapping)
* **Docs:** OpenAPI 3 (Swagger UI)
* **Build Tool:** Maven

---

## 🏃‍♂️ How to Run

### 1. Prerequisites
* Java JDK 21 or higher
* MySQL Server running

### 2. Configuration
Update `src/main/resources/application.properties`:

```properties
server.port=8990
spring.datasource.url=jdbc:mysql://localhost:3306/your_db_name
spring.datasource.username=root
spring.datasource.password=your_password
# Hibernate DDL
spring.jpa.hibernate.ddl-auto=update