# ✈️ Airline Reservation System – Backend API
A secure, concurrent, production-ready **Java Spring Boot** backend for managing users, flights, seat booking, cancellations, and admin operations.

Built for academic + practical use with real-world architectural patterns:
- Layered architecture
- **MySQL** database with **Spring Data JPA**
- Spring Security (USER + ADMIN)
- Thread-safe booking logic
- Scheduled system maintenance
- Complete API suite

---

## 🚀 Project Overview

This project implements a complete airline reservation backend with:

- User & Admin authentication
- Flight search, creation, update, deletion
- Seat booking with concurrency protection
- Booking cancellation workflow
- **MySQL persistence via Spring Data JPA** (auto-creates tables on startup)
- Database seeded with sample data on first run
- System scheduler for log cleanup & admin notifications
- Lightweight logging system

---

## 🧱 Tech Stack

| Technology | Purpose |
|-----------|---------|
| **Java 21** | Core language |
| **Spring Boot 3** | Web, DI, Scheduling |
| **Spring Data JPA** | ORM + Repository layer |
| **MySQL** | Relational database |
| **Hibernate** | JPA implementation (auto DDL) |
| **Spring Security** | AUTH + ROLE-based access |
| **BCrypt** | Password hashing |
| **Scheduled Tasks** | Automated log cleanup + notifications |

---

## 🏛️ System Architecture

```
Client
  ↓
Controller Layer
  ↓
Service Layer
  ↓
Repository Layer (Spring Data JPA Interfaces)
  ↓
MySQL Database
```

### ✔ Controllers
Handle API requests & authentication.

### ✔ Services
Contain business logic: booking, seat locking, validation.

### ✔ Repositories
Spring Data JPA interfaces — derived queries replace manual JSON read/write.

### ✔ Utilities
Logging via custom `LogUtil`.

### ✔ Scheduler
Runs system maintenance tasks automatically.

---

## 🗄️ Database Schema

Hibernate auto-creates the following tables on startup (`spring.jpa.hibernate.ddl-auto=update`):

| Table | Primary Key | Description |
|-------|-------------|-------------|
| `users` | `username` (natural key) | User accounts with BCrypt passwords |
| `flights` | `flight_id` | Flight details (airline, seats, price, status) |
| `flight_routes` | composite | Ordered route stops per flight (`@ElementCollection`) |
| `bookings` | `booking_id` (UUID, auto-generated) | Booking records with status tracking |

A `DataInitializer` component seeds **6 flights + 4 bookings** on first startup (only when tables are empty). The root admin user is created by `UserService`.

---

## 🔧 Setup & Configuration

### Prerequisites
- **Java 21**
- **MySQL** server running (local or remote)
- **Maven** (or use the included `mvnw` wrapper)

### Database Credentials

> ⚠️ **Credentials are NOT stored in source control.** Each developer creates their own local `.env` file.

1. Copy the template:
   ```bash
   cp .env.example .env
   ```

2. Edit `.env` with your MySQL credentials:
   ```
   DB_HOST=localhost
   DB_PORT=3306
   DB_NAME=airline_db
   DB_USERNAME=root
   DB_PASSWORD=your_password_here
   ```

3. The database (`airline_db`) is auto-created if it doesn't exist.

`application.properties` reads these via `${DB_USERNAME:root}` syntax (env vars with defaults).

---

## 🔒 Security Architecture

- **Spring Security** with Basic Auth
- Passwords stored using **BCrypt hashing**
- Role-based access:
    - `ROLE_USER` → booking, search
    - `ROLE_ADMIN` → manage users, flights, cancellations

---

## ⚙️ Concurrency & Thread Safety

### ✔ How Many Users Can Write at a Time?

### 1️⃣ Database Writes → **Managed by MySQL + JPA**
Spring Data JPA with MySQL handles concurrent writes through database-level transactions and row-level locking — far more robust than the previous file-based locking.

### 2️⃣ Seat Booking → **Per Flight Locking**
```java
synchronized (flightId.intern())
```

| Scenario | Allowed | Safe |
|---------|---------|------|
| 100 users booking **same flight** simultaneously | ✔ Yes | ✔ No double booking |
| 100 users booking **different flights** | ✔ Yes | ✔ Parallel execution |

---

## 🧾 Logging System

Custom `LogUtil` logs:

- User actions
- Admin actions
- Errors
- System events
- Scheduler events

Stored under:  `logs/log-YYYY-MM-DD.txt`


Scheduler removes logs older than **2 days**.

---

## ⏱ Scheduled Tasks

| Task | Frequency | Description |
|------|-----------|-------------|
| **Log Cleanup** | Daily @ midnight | Deletes old logs |
| **Admin Notification** | Every 30 min | Shows pending cancellation count |

---

# 📡 API Endpoints

## User Endpoints

| Method | Endpoint | Role |
|--------|----------|------|
| POST | `/api/users/register` | PUBLIC |
| POST | `/api/users/admin/register` | ADMIN |

---

## Flight Endpoints

| Method | Endpoint | Role |
|--------|----------|------|
| GET | `/api/flights` | USER, ADMIN |
| GET | `/api/flights/search?source=X&destination=Y` | USER, ADMIN |
| GET | `/api/flights/{flightId}` | USER, ADMIN |
| POST | `/api/flights` | ADMIN |
| PUT | `/api/flights/{flightId}/status` | ADMIN |
| DELETE | `/api/flights/{flightId}` | ADMIN |

---

## Booking Endpoints

| Method | Endpoint | Role |
|--------|----------|------|
| POST | `/api/bookings/{flightId}` | USER |
| GET | `/api/bookings/my-bookings` | USER |
| GET | `/api/bookings/all` | ADMIN |
| DELETE | `/api/bookings/{bookingId}` | ADMIN |
| POST | `/api/bookings/cancel-request/{bookingId}` | USER |
| GET | `/api/bookings/admin/cancel-requests` | ADMIN |
| POST | `/api/bookings/admin/cancel-all-requests` | ADMIN |
| POST | `/api/bookings/admin/cancel-request/{bookingId}` | ADMIN |

---

# ▶️ How to Run

1. **Set up MySQL credentials** — copy `.env.example` → `.env` and fill in your password
2. Open project in **IntelliJ** (or any IDE)
3. Run: `SimpleAirlineReservationSystemApplication.java`
   — or from terminal: `./mvnw spring-boot:run`

Server URL:
`http://localhost:8080`

On first startup, Hibernate creates the tables and seed data is inserted automatically.

---

# 👥 Team: Java Wizards

| Member | ID | Contribution                      |
|--------|----|-----------------------------------|
| Amballa Pardhiv | BT2024071 | Services + Async + Concurrency    |
| Thummala Hemanth Reddy | BT2024105 | Services + Config                 |
| Chevuru V R Dinesh Karthik | BT20240199 | Controllers + Exceptions + Models |
| Parimi Venkata Krishna | BT2024161 | Persistence + JPA Migration       |
| Pidela Yashwanth Reddy | BT2024103 | Integration + Controllers         |
| Penumaka Sai Pramod | BT2024145 | Repository logic + Util           |

---

## ⚠️ System Limitations

| Area            | Limitation                                |
|-----------------|-------------------------------------------|
| Concurrency     | App-level `synchronized` for seat booking (single-instance only) |
| Scaling         | Not ideal for >1000 concurrent users without connection pooling tuning |
| Authentication  | Uses Basic Auth (no JWT or sessions)       |
| Logs            | Stored locally; only 2-day retention       |

---

## 🚀 Future Enhancements

- JWT Authentication
- Real-time seat map system
- Email/SMS notifications
- Admin dashboard UI
- Connection pooling (HikariCP tuning)

---

## 📄 License
For academic use.
