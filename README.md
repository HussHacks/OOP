# Jobsy - Job Application Platform

A full-stack job application platform built with **Javalin** (backend) and **React** (frontend).

## 🚀 Quick Start

### Backend (Javalin)

```bash
cd jobsy-backend
mvn clean package
java -jar target/jobsy-backend-1.0.0.jar
```

Server starts on: `http://localhost:8080`

### Frontend (React)

```bash
cd jobsy-frontend
npm install
npm start
```

Frontend runs on: `http://localhost:3000`

---

## 📋 Features

- **User Authentication**: Student and Employer registration/login
- **Job Listings**: Browse and search available jobs
- **Job Applications**: Students can apply to jobs
- **Application Management**: Employers can accept/reject applications
- **Profile Management**: User profiles with skills and experience

---

## 🛠️ Tech Stack

### Backend
- **Framework**: Javalin 5.6.3
- **Database**: H2 (embedded)
- **Connection Pool**: HikariCP
- **JSON**: Jackson
- **Build**: Maven

### Frontend
- **Framework**: React 19
- **Routing**: React Router
- **Styling**: Tailwind CSS
- **Animations**: Framer Motion
- **HTTP**: Axios

---

## 📡 API Endpoints

### Authentication
- `POST /api/users/signup` - Register new user
- `POST /api/users/login` - Login

### Jobs
- `GET /jobs` - List all jobs
- `GET /jobs/{id}` - Get job by ID
- `POST /jobs` - Create job (employer)
- `PUT /jobs/{id}` - Update job
- `DELETE /jobs/{id}` - Delete job
- `GET /jobs/search?keyword=` - Search jobs

### Applications
- `POST /applications` - Submit application
- `GET /applications/student/{id}` - Get student's applications
- `GET /applications/job/{id}` - Get job's applications
- `PUT /applications/{id}/accept` - Accept application
- `PUT /applications/{id}/reject` - Reject application

---

## 🗄️ Database

H2 embedded database with auto-created tables:
- `users` (students and employers)
- `jobs`
- `applications`

**H2 Console**: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:file:./data/jobsydb`
- Username: `sa`
- Password: (empty)

---

## 📁 Project Structure

```
jobsy-backend/
├── src/main/java/com/jobsy/
│   ├── JavalinApp.java          # Main application
│   ├── config/
│   │   └── DatabaseConfig.java  # DB setup
│   ├── dao/
│   │   ├── UserDAO.java         # User data access
│   │   ├── JobDAO.java          # Job data access
│   │   └── ApplicationDAO.java  # Application data access
│   ├── models/
│   │   ├── User.java, Student.java, Employer.java
│   │   ├── Job.java
│   │   └── Application.java
│   └── services/
│       ├── UserService.java
│       ├── JobService.java
│       └── ApplicationService.java

jobsy-frontend/
├── src/
│   ├── pages/
│   │   ├── Home.jsx
│   │   ├── Jobs.jsx
│   │   ├── Login.jsx
│   │   ├── Signup.jsx
│   │   └── Profile.jsx
│   ├── components/
│   │   └── Navbar.jsx
│   └── api.js                   # API integration
```

---

## 🔧 Development

### Backend Development
```bash
cd jobsy-backend
mvn compile
mvn exec:java -Dexec.mainClass="com.jobsy.JavalinApp"
```

### Frontend Development
```bash
cd jobsy-frontend
npm start
```

---

## 📦 Build for Production

### Backend
```bash
cd jobsy-backend
mvn clean package
# Creates: target/jobsy-backend-1.0.0.jar
```

### Frontend
```bash
cd jobsy-frontend
npm run build
# Creates: build/ directory
```

---

## 🎯 Why Javalin?

- ✅ **Lightweight**: 1MB vs Spring Boot's 30MB
- ✅ **Fast**: <1s startup vs 3-5s
- ✅ **Simple**: Explicit routing, no magic
- ✅ **Easy to Learn**: Clear code flow
- ✅ **Production Ready**: Used by many companies

---

## 📝 License

This is an educational project for learning full-stack development.

---

## 🤝 Contributing

This is a student project. Feel free to fork and experiment!

---

## 📞 Support

For issues or questions, check the code documentation or create an issue.
