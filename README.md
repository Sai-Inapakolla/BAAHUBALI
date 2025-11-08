# 🏦 BAAHUBALI Bank Management System

> **Secure Banking Made Simple** - A comprehensive banking application with role-based access control, transaction management, and loan processing.

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

---

## 📋 Table of Contents
- [Features](#-features)
- [Quick Start](#-quick-start)
- [Test Accounts](#-test-accounts)
- [API Documentation](#-api-documentation)
- [Architecture](#-architecture)
- [Screenshots](#-screenshots)
- [Documentation](#-documentation)

---

## ✨ Features

### 🔐 Authentication & Authorization
- ✅ User Registration & Login
- ✅ Role-Based Access Control (Admin, Employee, Customer)
- ✅ Secure Password Hashing (BCrypt)
- ✅ Token-Based Authentication

### 💰 Banking Operations
- ✅ **Deposit Money** - Add funds to account
- ✅ **Withdraw Money** - Withdraw with balance validation
- ✅ **Transfer Money** - Send money to other users by email
- ✅ **Transaction History** - Complete audit trail

### 🏦 Loan Management
- ✅ **Four Loan Types**:
  - Educational Loan (8.5% interest)
  - Farming Loan (6.0% interest)
  - Gold Loan (12.0% interest)
  - Personal Loan (15.0% interest)
- ✅ **Loan Application** - Apply with amount, tenure, purpose
- ✅ **Loan Approval/Rejection** - Admin/Employee can approve/reject
- ✅ **Auto-Disbursement** - Approved loans automatically credit account
- ✅ **Loan Tracking** - View status and admin comments

### 👥 Role-Based Dashboards
- ✅ **Customer Dashboard** - Banking operations, loan applications
- ✅ **Employee Dashboard** - Customer service, loan processing
- ✅ **Admin Dashboard** - User management, system statistics, full control

---

## 🚀 Quick Start

### Prerequisites
- Java 21 or higher
- Maven 3.6+
- Any modern web browser

### Installation

1. **Clone the repository**
```bash
git clone <repository-url>
cd BANK-2
```

2. **Build the project**
```bash
mvn clean package
```

3. **Run the application**
```bash
mvn spring-boot:run
```

4. **Access the application**
```
http://localhost:8080
```

5. **Access H2 Database Console** (optional)
```
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:file:./data/baahubali-bank
Username: sa
Password: (leave empty)
```

---

## 👤 Test Accounts

| Role | Email | Password | Initial Balance |
|------|-------|----------|----------------|
| **Admin** | admin@baahubali.com | admin123 | $0.00 |
| **Employee** | employee@baahubali.com | employee123 | $0.00 |
| **Customer** | alice@baahubali.com | password | $1,000.00 |
| **Customer** | bob@baahubali.com | password | $500.00 |

---

## 📡 API Documentation

### Authentication Endpoints
```http
POST /api/auth/register
POST /api/auth/login
GET  /api/auth/role?email={email}
```

### Transaction Endpoints
```http
POST /api/transactions                    # Deposit/Withdraw
POST /api/transactions/transfer-by-email  # Transfer money
GET  /api/transactions/my-transactions    # View history
```

### Loan Endpoints
```http
POST /api/loans/apply              # Apply for loan
GET  /api/loans/my-loans           # View my loans
GET  /api/loans/pending            # View pending (Admin/Employee)
GET  /api/loans/all                # View all (Admin/Employee)
POST /api/loans/{id}/approve       # Approve loan
POST /api/loans/{id}/reject        # Reject loan
```

### User Management (Admin Only)
```http
GET    /api/users                  # List all users
POST   /api/users                  # Create user
GET    /api/users/by-email/{email} # Find by email
PUT    /api/users/{id}             # Update user
DELETE /api/users/{id}             # Delete user
```

**Authentication**: All protected endpoints require `X-Auth-Token` header

---

## 🏗️ Architecture

```
Frontend (HTML/CSS/JS)
        ↓
Spring Boot REST API
        ↓
Spring Data JPA
        ↓
H2 Database (File-based)
```

### Technology Stack
- **Backend**: Spring Boot 3.5.0, Java 21
- **Security**: Spring Security, BCrypt
- **Database**: H2 (Development), JPA/Hibernate
- **Frontend**: HTML5, CSS3, Vanilla JavaScript
- **Build Tool**: Maven

---

## 📸 Screenshots

### Customer Dashboard
- View balance and transaction history
- Deposit, withdraw, and transfer money
- Apply for loans and track status

### Employee Dashboard
- Search and manage customers
- Process transactions for customers
- Review and approve/reject loans

### Admin Dashboard
- View system statistics
- Manage all users and employees
- Full loan management capabilities

---

## 📚 Documentation

Comprehensive documentation is available in the following files:

1. **[BANK_APPLICATION_FEATURES.md](BANK_APPLICATION_FEATURES.md)**
   - Complete feature list
   - Access control matrix
   - Database schema
   - API endpoints

2. **[TESTING_GUIDE.md](TESTING_GUIDE.md)**
   - Test scenarios
   - Step-by-step testing instructions
   - Expected results
   - Error testing

3. **[SYSTEM_ARCHITECTURE.md](SYSTEM_ARCHITECTURE.md)**
   - System diagrams
   - Request flow
   - Security layers
   - Technology stack details

---

## 🔒 Security Features

- ✅ BCrypt password hashing
- ✅ Token-based authentication
- ✅ Role-based authorization
- ✅ Input validation
- ✅ SQL injection prevention (JPA)
- ✅ Balance validation (prevent overdraft)
- ✅ Transaction atomicity

---

## 🎯 Key Highlights

### For Customers
- Simple and intuitive banking interface
- Real-time balance updates
- Easy money transfers by email
- Quick loan applications
- Track loan status with admin comments

### For Employees
- Efficient customer service tools
- Process transactions on behalf of customers
- Review and process loan applications
- Search customers by email

### For Admins
- Complete system oversight
- User and employee management
- System statistics and analytics
- Full loan management control

---

## 🛠️ Development

### Project Structure
```
src/
├── main/
│   ├── java/com/bank/
│   │   ├── controller/      # REST Controllers
│   │   ├── service/         # Business Logic
│   │   ├── repository/      # Data Access
│   │   ├── entity/          # JPA Entities
│   │   ├── config/          # Configuration
│   │   ├── util/            # Utilities
│   │   ├── loan/            # Loan Types & Status
│   │   └── user/            # User Roles
│   └── resources/
│       ├── static/          # Frontend files
│       └── application.properties
```

### Build Commands
```bash
# Clean build
mvn clean package

# Run application
mvn spring-boot:run

# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Create executable JAR
mvn clean package
java -jar target/bank-mvp-0.0.1-SNAPSHOT.jar
```

---

## 🔄 Future Enhancements

- [ ] Loan repayment system
- [ ] EMI calculator
- [ ] Interest calculation over time
- [ ] Email notifications
- [ ] SMS alerts
- [ ] Account statements/reports
- [ ] Transfer to other banks (external integration)
- [ ] Mobile app
- [ ] Two-factor authentication

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👥 Contributors

- **Development Team** - Initial work and ongoing maintenance

---

## 🤝 Support

For support, email support@baahubali.com or open an issue in the repository.

---

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- H2 Database for the embedded database solution
- All contributors and testers

---

**Built with ❤️ using Spring Boot**

---

## 📞 Contact

- **Website**: https://baahubali-bank.com
- **Email**: info@baahubali.com
- **GitHub**: https://github.com/baahubali-bank

---

*Last Updated: October 2025*