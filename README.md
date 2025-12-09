# ✈️ Airline Reservation System – Backend API
A secure, concurrent, production-ready **Java Spring Boot** backend for managing users, flights, seat booking, cancellations, and admin operations.

Built for academic + practical use with real-world architectural patterns:
- Layered architecture
- JSON-based persistence
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
- File-based persistence (no DB needed)
- System scheduler for log cleanup & admin notifications
- Lightweight logging system

---

## 🧱 Tech Stack

| Technology | Purpose |
|-----------|---------|
| **Java 21** | Core language |
| **Spring Boot 3** | Web, DI, Scheduling |
| **Spring Security** | AUTH + ROLE-based access |
| **BCrypt** | Password hashing |
| **JSON File Storage** | Persistent data w/o DB |
| **Scheduled Tasks** | Automated log cleanup + notifications |

---

## 🏛️ System Architecture

Client
↓
Controller Layer
↓
Service Layer
↓
Repository Layer (Thread-Safe)
↓
JSON File Storage

### ✔ Controllers
Handle API requests & authentication.

### ✔ Services
Contain business logic: booking, seat locking, validation.

### ✔ Repositories
Read/write JSON files with **ReentrantReadWriteLock** for concurrency safety.

### ✔ Utilities
Logging + JSON read/write.

### ✔ Scheduler
Runs system maintenance tasks automatically.

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

### 1️⃣ JSON Repositories → **One Writer at a Time**
Uses:
ReentrantReadWriteLock writeLock

Meaning:
- Unlimited concurrent reads
- Only ONE write at a time
- Prevents data corruption

### 2️⃣ Seat Booking → **Per Flight Locking**
synchronized (flightId.intern())

| Scenario | Allowed | Safe |
|---------|---------|------|
| 100 users booking **same flight** simultaneously | ✔ Yes | ✔ No double booking |
| 100 users booking **different flights** | ✔ Yes | ✔ Parallel execution |

---

## 🗂 JSON File Storage Structure
data/
├── users.json
├── flights.json
└── bookings.json

---

## 🧾 Logging System

Custom `LogUtil` logs:

- User actions
- Admin actions
- Errors
- System events
- Scheduler events

Stored under:

logs/log-YYYY-MM-DD.txt


Scheduler removes logs older than **3 days**.

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

1. Open project in **IntelliJ**
2. Run: SimpleAirlineReservationSystemApplication.java

Server URL:
http://localhost:8080

---

# 👥 Team: Java Wizards

| Member | ID | Contribution                      |
|--------|----|-----------------------------------|
| Amballa Pardhiv | BT2024071 | Services + Async + Concurrency    |
| Thummala Hemanth Reddy | BT2024105 | Services + Config                 |
| Chevuru V R Dinesh Karthik | BT20240199 | Controllers + Exceptions + Models |
| Parimi Venkata Krishna | BT2024161 | Persistence + JSON handling       |
| Pidela Yashwanth Reddy | BT2024103 | Integration + Controllers         |
| Penumaka Sai Pramod | BT2024145 | Repository logic + Util           |

---

## ⚠️ System Limitations

| Area            | Limitation                                |
|-----------------|--------------------------------------------|
| JSON Storage    | Not suitable for large datasets            |
| Concurrency     | Only one write operation allowed at a time |
| Scaling         | Not ideal for >1000 concurrent users       |
| Authentication  | Uses Basic Auth (no JWT or sessions)       |
| Logs            | Stored locally; only 3-day retention       |
| Transactions    | No ACID guarantees due to file-based storage |

---

## 🚀 Future Enhancements

- PostgreSQL/MySQL migration
- JWT Authentication
- Real-time seat map system
- Email/SMS notifications
- Admin dashboard UI

---

## 📄 License
For academic use.
