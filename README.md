# JPetStore Partial — Modernized Spring Boot REST API

![Java 17](https://img.shields.io/badge/Java-17_LTS-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.4-brightgreen.svg)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue.svg)
![MyBatis](https://img.shields.io/badge/MyBatis-3.0.3-red.svg)
![Docker](https://img.shields.io/badge/Docker_Compose-Ready-blue.svg)

`jpetstore-partial` is a modernized REST API microservice built with **Spring Boot 3** and **PostgreSQL**. It serves as the modernized backend layer for the legacy JPetStore application ([jpetstore-6](https://github.com/mybatis/jpetstore-6)), adopting a **Strangler Fig / Hybrid Migration Pattern**.

---

## 🏛️ Architecture Overview

```
 ┌────────────────────────────────────────────────────────┐
 │           Vistas JSP Legacy (jpetstore-6)              │
 │   • Mantiene maquetación, CSS y diseño original        │
 │   • Invoca llamadas client-side (fetch())              │
 └──────────────────────────┬─────────────────────────────┘
                            │ REST API (JSON sobre HTTP)
                            ▼
 ┌────────────────────────────────────────────────────────┐
 │       jpetstore-partial (Spring Boot REST API)         │
 │   • Java 17 + Spring Boot 3.3.4                        │
 │   • Layered Architecture (Controller, Service, Mapper) │
 │   • MyBatis 3 ORM                                      │
 └──────────────────────────┬─────────────────────────────┘
                            │ JDBC (Puerto 5432)
                            ▼
 ┌────────────────────────────────────────────────────────┐
 │              PostgreSQL Database (Shared DB)           │
 │              Database: jpetstore                       │
 └────────────────────────────────────────────────────────┘
```

---

## 🚀 Tech Stack

* **Language:** Java 17 (Temurin OpenJDK)
* **Framework:** Spring Boot 3.3.4 (LTS)
* **Persistence & ORM:** MyBatis Spring Boot Starter 3.0.3 + Spring JDBC
* **Database:** PostgreSQL 16
* **Build System:** Apache Maven 3.9 (via `mvnw` wrapper)
* **Containerization:** Multi-stage `Dockerfile` & `docker-compose.yml`

---

## 📡 REST API Endpoints

### Active Endpoints

| Method | Endpoint | Description | Issue Status |
|---|---|---|---|
| `GET` | `/api/hello` | Health check & PostgreSQL version query | ✅ Done |
| `GET` | `/api/catalog/categories` | Retrieves all pet categories | ✅ Done ([#1](https://github.com/CarlosDBayona/jpetstore-partial/issues/1)) |
| `GET` | `/api/catalog/categories/{categoryId}/products` | Retrieves products by category ID | ⏳ In Progress ([#2](https://github.com/CarlosDBayona/jpetstore-partial/issues/2)) |
| `GET` | `/api/catalog/products/{productId}` | Product details | 📅 Planned ([#3](https://github.com/CarlosDBayona/jpetstore-partial/issues/3)) |
| `GET` | `/api/catalog/products/{productId}/items` | Item variants for a product | 📅 Planned ([#4](https://github.com/CarlosDBayona/jpetstore-partial/issues/4)) |
| `GET` | `/api/catalog/items/{itemId}` | Item details & inventory stock | 📅 Planned ([#5](https://github.com/CarlosDBayona/jpetstore-partial/issues/5)) |
| `GET` | `/api/catalog/products/search?keyword=` | Search products by keyword | 📅 Planned ([#6](https://github.com/CarlosDBayona/jpetstore-partial/issues/6)) |

---

## 🛠️ Quick Start & Running Locally

### Option 1: Full Stack with Docker Compose (Recommended)

Requires Docker Desktop installed.

```bash
# Clone the repository
git clone https://github.com/CarlosDBayona/jpetstore-partial.git
cd jpetstore-partial

# Build and start PostgreSQL + Spring Boot API
docker compose up --build -d
```

Verify the API is live:
```bash
curl http://localhost:8080/api/hello
```

### Option 2: Run Spring Boot Locally (with Dockerized PostgreSQL)

1. Start PostgreSQL:
   ```bash
   docker compose up postgres -d
   ```
2. Run Spring Boot application via Maven wrapper:
   ```bash
   # On Windows PowerShell / CMD:
   .\mvnw.cmd spring-boot:run

   # On Linux / macOS:
   ./mvnw spring-boot:run
   ```

---

## 🧪 Running Tests

Execute the unit and integration test suite:

```bash
.\mvnw.cmd test
```

---

## 📄 Architectural Documentation

Detailed migration blueprints are available in the repository:
* 📘 [Modernization API-First Plan](plans/MODERNIZATION_API_FIRST_PLAN.md)
* 📗 [Shared PostgreSQL Persistence Plan](plans/POSTGRESQL_SHARED_PERSISTENCE_PLAN.md)
