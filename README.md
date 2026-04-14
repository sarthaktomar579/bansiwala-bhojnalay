# Bansiwala Bhojnalay - Mess Meal Tracker

A complete meal tracking system for student mess in Indore. Supports **QR code scanning**, **fingerprint authentication** (with dummy testing mode), and **manual check-in**.

## Tech Stack

| Layer       | Technology               |
|-------------|--------------------------|
| Backend     | Java 17 + Spring Boot 3  |
| Frontend    | Angular 19               |
| Database    | PostgreSQL               |
| QR Code     | ZXing (Google)           |
| Fingerprint | Pluggable SDK (Mantra MFS100 ready) |

## Project Structure

```
Bansiwala_Bhojnalay_Records/
├── backend/              ← Spring Boot API
│   ├── src/main/java/com/bansiwala/bhojnalay/
│   │   ├── controller/   ← REST APIs
│   │   ├── service/      ← Business logic
│   │   ├── repository/   ← Database queries
│   │   ├── entity/       ← JPA entities
│   │   ├── dto/          ← Request/Response objects
│   │   ├── enums/        ← MealType, CheckInMethod
│   │   ├── exception/    ← Error handling
│   │   └── config/       ← CORS config
│   └── pom.xml
├── frontend/             ← Angular UI
│   └── src/app/
│       ├── features/     ← Pages (Dashboard, Check-in, Students, Reports)
│       ├── core/         ← Services, Models
│       └── shared/       ← Reusable components
└── README.md
```

## Prerequisites

1. **Java 17+** — Download from https://adoptium.net/
2. **PostgreSQL** — Download from https://www.postgresql.org/download/
3. **Node.js 18+** — Already installed

## Setup

### 1. Database Setup

Open pgAdmin or psql and create the database:

```sql
CREATE DATABASE bansiwala_bhojnalay;
```

Update credentials in `backend/src/main/resources/application.properties` if needed:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bansiwala_bhojnalay
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### 2. Backend Setup

```bash
cd backend

# On Windows (PowerShell)
./mvnw.cmd spring-boot:run

# Or if you have Maven installed
mvn spring-boot:run
```

Backend starts at **http://localhost:8080**

### 3. Frontend Setup

```bash
cd frontend
npm install        # (already done)
ng serve
```

Frontend starts at **http://localhost:4200**

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/students` | Register student |
| GET | `/api/students` | List all students |
| GET | `/api/students/{id}` | Get student by ID |
| PUT | `/api/students/{id}` | Update student |
| GET | `/api/students/{id}/qr` | Download QR code PNG |
| PATCH | `/api/students/{id}/fingerprint` | Register fingerprint |
| POST | `/api/meals/check-in/qr` | Check-in via QR |
| POST | `/api/meals/check-in/fingerprint` | Check-in via fingerprint |
| POST | `/api/meals/check-in/manual/{id}` | Manual check-in |
| GET | `/api/meals/today` | Today's records |
| GET | `/api/reports/daily` | Daily summary |
| GET | `/api/reports/student/{id}/monthly` | Monthly report |

## Fingerprint Scanner Integration

The system has two modes:

### Dummy Mode (Default — for testing without hardware)
- Toggle "Dummy Mode" on the Check-In page
- Select a test student from dropdown
- Pre-configured dummy templates are matched against the database

### Real Device Mode (when you have the scanner)
- Edit `frontend/src/app/core/services/fingerprint.service.ts`
- Uncomment the real device code in the `captureReal()` method
- Configure your device URL (e.g., Mantra MFS100 runs at `https://localhost:11100`)

## Meal Time Windows

Configured in `application.properties`:

| Meal | Time Window |
|------|-------------|
| Breakfast | 07:00 - 10:30 |
| Lunch | 11:00 - 15:30 |
| Dinner | 18:00 - 22:00 |
