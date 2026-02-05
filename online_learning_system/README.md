# Online Learning System – Spring Boot Microservices

This project is an **Online Learning System** developed using **Spring Boot Microservices Architecture**.  

It consists of multiple independent services communicating via REST APIs, with an **API Gateway** acting as a single entry point.

---

## 📌 Services

### 🔹 API Gateway

- Routes client requests to backend services 
- Acts as a single access point for the system

### 🔹 Student Management Service

- Handles student-related operations
- Provides REST APIs for managing student data

### 🔹 Course Management Service

- Handles course-related operations
- Provides REST APIs for managing courses

---

## 🛠 Tech Stack

- Java
  
- Spring Boot
  
- Spring Cloud Gateway
  
- REST APIs
  
- Maven

---

## 📂 Project Structure

Online_Learning_System

│

├── api-gateway

├── course-management-system

├── student-management-system

└── .gitignore

---

## 🔐 Configuration

Sensitive configuration files are excluded using .gitignore.

Create your own:

application.properties

application.yml


