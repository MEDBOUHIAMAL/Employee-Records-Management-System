# 📂 Employee Records Management System

An **internal Employee Records Management System** designed to centralize the management of employee data across departments, ensuring efficient and secure access for different roles.

---

## 🛠 Features

- **Centralized Employee Data**: Maintain and update employee records with accuracy.
- **Role-Based Access Control**: Secure access for Admins, Managers, and HR personnel.
- **Advanced Search & Filters**: Quickly locate employees using powerful search and filtering tools.
- **HR Personnel**: Can perform CRUD (Create, Read, Update, Delete) operations on all employee data.
- **Managers**: Can view and update specific details for employees within their department.
- **Administrators**: Full system access, including configuration settings and managing user permissions

---


## 🚀 Getting Started

Follow these steps to set up and run the project on your local machine.

### Prerequisites

Make sure you have the following installed:

- **Java 17**
- **Oracle Database** (or Oracle XE for local use)
- **Docker** and **Docker Compose**

---

### 📂 Project Structure

```plaintext
Employee-Records-Management-System/
├── ERMS/                    # Backend (Spring Boot)
├── EmployeeManagementUI/    # Frontend (Swing)
├── docker-compose.yml       # Docker configuration
├── Collection.postman_collection.json  # Postman API collection
├── README.md                # Documentation

```

---

## 🐳 Docker Setup

To set up the application using Docker, follow these steps:

### 1. Build and start the application

Use Docker Compose to build and run the application:

```bash
docker-compose up --build
```

---

## 📚 Swagger Documentation

The API is fully documented using Swagger. Navigate to:

```bash
http://localhost:8080/swagger-ui/index.html
```

---

## 🧪 Testing

Run all unit and integration tests using Maven:

```bash
cd ERMS
mvn test
```

---

## 🧰 Tools and Technologies

- **Backend: Spring Boot, Hibernate, Oracle Database**.
- **Frontend: Swing (Java GUI)**.
- **Testing: JUnit, Mockito**.
- **Containerization: Docker, Docker Compose**.
- **API Documentation: Swagger**.
- **Dependency Management: Maven**.










