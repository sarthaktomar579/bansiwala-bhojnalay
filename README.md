# Bansiwala Bhojnalay - Meal Tracker

A full-stack meal tracking system for **Bansiwala Bhojnalay, Indore**.  
Members can check in for Lunch/Dinner, admin can manage members, payments, and reports.

## Live URLs

- **Frontend (Vercel):** https://bansiwala-bhojnalay.vercel.app/
- **Backend (Render):** https://bansiwala-bhojnalay.onrender.com/
- **Database:** Neon Cloud PostgreSQL (no local DB needed)

## Tech Stack

| Layer      | Technology                   |
|------------|------------------------------|
| Frontend   | Angular 19+, TypeScript      |
| Backend    | Java 17+, Spring Boot 3.4    |
| Database   | PostgreSQL (Neon Cloud)      |
| Auth       | JWT (Spring Security)        |
| Deployment | Vercel (FE), Render (BE)     |

## Admin Login

- **Username:** `admin`
- **Password:** `bansiwala2026`

---

## Setup on a New Laptop (Windows)

### Prerequisites — Install These First

1. **Java 17+** (Temurin recommended)
   - Download: https://adoptium.net/temurin/releases/
   - Pick **Windows x64** → `.msi` installer
   - During install, check **"Set JAVA_HOME"** and **"Add to PATH"**

2. **Node.js 18+**
   - Download: https://nodejs.org/ (pick LTS version)
   - Install with default settings

3. **Git** (optional, for cloning from GitHub)
   - Download: https://git-scm.com/downloads/win

### Option A: Clone from GitHub (Recommended)

```
git clone https://github.com/sarthaktomar579/bansiwala-bhojnalay.git
cd bansiwala-bhojnalay
```

### Option B: Use the ZIP file

1. Copy the `Bansiwala_Bhojnalay_Records.zip` file to the new laptop
2. Extract/unzip it to any folder
3. Open terminal in that folder

### Quick Start (3 Steps)

**Step 1:** Run setup (first time only — installs all dependencies)
```
setup.bat
```

**Step 2:** Start backend (keep this terminal open)
```
start-backend.bat
```

**Step 3:** Open a NEW terminal, start frontend
```
start-frontend.bat
```

The app opens at **http://localhost:4200**

---

## Manual Setup (if batch scripts don't work)

### Backend

```bash
cd backend
.\mvnw.cmd spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npx ng serve --open
```

---

## Project Structure

```
Bansiwala_Bhojnalay_Records/
├── backend/                    # Java Spring Boot API
│   ├── src/main/java/          # Java source code
│   ├── src/main/resources/     # application.properties
│   ├── pom.xml                 # Maven dependencies
│   ├── mvnw.cmd                # Maven wrapper (no install needed)
│   └── Dockerfile              # For cloud deployment
├── frontend/                   # Angular SPA
│   ├── src/app/                # Angular components & services
│   ├── package.json            # npm dependencies
│   └── vercel.json             # Vercel deployment config
├── setup.bat                   # First-time setup script
├── start-backend.bat           # Start backend server
├── start-frontend.bat          # Start frontend dev server
└── README.md                   # This file
```

## Features

- Manual check-in (admin) & QR scan check-in (members)
- Lunch & Dinner tracking with thali count (1-30)
- Monthly calendar report per member
- Payment tracking with repeating 30-thali due cycles
- Voice confirmation on QR check-in
- Role-based access (Admin / Member)
- Mobile responsive design
- Auto-refreshing dashboard (every 15 seconds)

## Important Notes

- The database is hosted on **Neon Cloud** — no local PostgreSQL needed
- Your internet connection must be active for the app to work (it connects to Neon)
- If your WiFi blocks database connections, use a **mobile hotspot**
- Backend runs on port **8080**, frontend on port **4200**
