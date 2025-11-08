# BAAHUBALI Bank Management System - Complete Feature Documentation

## 🏦 Overview
A comprehensive banking application with role-based access control, transaction management, and loan processing capabilities.

---

## 🔐 1. Authentication & Authorization

### Features Implemented:
- ✅ **User Registration (Sign Up)**
  - Endpoint: `POST /api/auth/register`
  - Creates new customer accounts
  - Automatic password hashing with BCrypt
  - Returns JWT token for immediate login

- ✅ **User Login (Sign In)**
  - Endpoint: `POST /api/auth/login`
  - Email and password authentication
  - Returns JWT token with user role
  - Updates last login timestamp

- ✅ **Role-Based Access Control**
  - Three user roles: ADMIN, EMPLOYEE, CUSTOMER
  - Token-based authentication (X-Auth-Token header)
  - Role verification for protected endpoints

---

## 💰 2. Transaction Management

### A. Deposit Money
- ✅ **Endpoint**: `POST /api/transactions`
- ✅ **Type**: DEPOSIT
- ✅ **Features**:
  - Add money to user account
  - Updates user balance immediately
  - Creates transaction record with timestamp
  - Optional description field
  - Transaction counter incremented

### B. Withdraw Money
- ✅ **Endpoint**: `POST /api/transactions`
- ✅ **Type**: WITHDRAW
- ✅ **Features**:
  - Withdraw money from account
  - Validates sufficient balance
  - Updates user balance immediately
  - Creates transaction record
  - Prevents overdraft

### C. Transfer Within Same Bank
- ✅ **Endpoint**: `POST /api/transactions/transfer-by-email`
- ✅ **Features**:
  - Transfer money to another user by email
  - Validates sender has sufficient funds
  - Prevents self-transfer
  - Creates two transaction records (debit & credit)
  - Updates both sender and receiver balances
  - Transaction descriptions include sender/receiver names

### D. Transfer to Other Bank
- ⚠️ **Status**: Not implemented (same bank transfers only)
- **Recommendation**: Would require:
  - External bank API integration
  - IFSC/SWIFT code validation
  - Inter-bank settlement system
  - Additional transaction fees

---

## 📊 3. Transaction History
- ✅ **View Own Transactions**: `GET /api/transactions/my-transactions`
- ✅ **View User Transactions (Admin)**: `GET /api/transactions/user/{id}`
- ✅ **Features**:
  - Ordered by timestamp (newest first)
  - Shows transaction type, amount, description
  - Displays sender/receiver information for transfers

---

## 🏦 4. Loan Management System

### A. Loan Types & Interest Rates
✅ **Four Loan Categories**:

| Loan Type | Interest Rate | Description |
|-----------|--------------|-------------|
| EDUCATIONAL | 8.5% | For education expenses |
| FARMING | 6.0% | Agricultural loans |
| GOLD | 12.0% | Gold-backed loans |
| PERSONAL | 15.0% | Personal loans |

### B. Loan Application Process
- ✅ **Endpoint**: `POST /api/loans/apply`
- ✅ **Customer Features**:
  - Apply for any loan type
  - Specify loan amount (minimum $1000)
  - Choose tenure (1-120 months)
  - Provide purpose/reason
  - Interest rate auto-assigned based on loan type
  - Initial status: PENDING

### C. Loan Status Workflow
✅ **Five Status States**:
1. **PENDING** - Awaiting approval
2. **APPROVED** - Approved by admin/employee
3. **REJECTED** - Rejected by admin/employee
4. **DISBURSED** - Loan amount credited
5. **COMPLETED** - Loan fully repaid

### D. Loan Approval System
- ✅ **Who Can Approve**: ADMIN and EMPLOYEE roles
- ✅ **Approval Endpoint**: `POST /api/loans/{loanId}/approve`
- ✅ **Features**:
  - Admin/Employee can approve pending loans
  - Add approval comments
  - Records approval date and approver ID
  - **Auto-disburses loan amount** to user account
  - Updates user balance immediately
  - Creates/updates account record

### E. Loan Rejection System
- ✅ **Who Can Reject**: ADMIN and EMPLOYEE roles
- ✅ **Rejection Endpoint**: `POST /api/loans/{loanId}/reject`
- ✅ **Features**:
  - Admin/Employee can reject pending loans
  - Mandatory rejection reason/comments
  - Records rejection date and rejector ID

### F. Loan Viewing Permissions
- ✅ **View My Loans**: `GET /api/loans/my-loans` (All users)
- ✅ **View Pending Loans**: `GET /api/loans/pending` (Admin/Employee)
- ✅ **View All Loans**: `GET /api/loans/all` (Admin/Employee)

---

## 👥 5. Role-Based Dashboards

### A. CUSTOMER Dashboard
✅ **Access**: All registered customers
✅ **Features**:
- View account balance
- View transaction history
- Deposit money
- Withdraw money
- Transfer to other users (by email)
- Apply for loans
- View loan applications and status
- See loan approval/rejection comments

### B. EMPLOYEE Dashboard
✅ **Access**: Employee role only
✅ **Features**:
- Search customers by email
- View customer information
- Process deposits for customers
- Process withdrawals for customers
- View customer transaction history
- **Review pending loan applications**
- **Approve/Reject loans**
- View all loans in system
- Add approval/rejection comments

### C. ADMIN Dashboard
✅ **Access**: Admin role only
✅ **Features**:
- **All Employee features PLUS:**
- View all users in system
- Create new users (any role)
- View system statistics:
  - Total users by role
  - Total balance in system
  - Total transactions
  - Loan statistics
- Manage employees
- Create new employees
- View all transactions
- **Full loan management**
- Delete users (if needed)
- Update user roles

---

## 🔒 6. Access Control Matrix

| Feature | Customer | Employee | Admin |
|---------|----------|----------|-------|
| Register/Login | ✅ | ✅ | ✅ |
| View Own Balance | ✅ | ✅ | ✅ |
| Deposit (Self) | ✅ | ✅ | ✅ |
| Withdraw (Self) | ✅ | ✅ | ✅ |
| Transfer Money | ✅ | ✅ | ✅ |
| View Own Transactions | ✅ | ✅ | ✅ |
| Apply for Loan | ✅ | ✅ | ✅ |
| View Own Loans | ✅ | ✅ | ✅ |
| Search Customers | ❌ | ✅ | ✅ |
| Process Customer Transactions | ❌ | ✅ | ✅ |
| View Pending Loans | ❌ | ✅ | ✅ |
| Approve/Reject Loans | ❌ | ✅ | ✅ |
| View All Loans | ❌ | ✅ | ✅ |
| View All Users | ❌ | ❌ | ✅ |
| Create Users | ❌ | ❌ | ✅ |
| View System Stats | ❌ | ❌ | ✅ |
| Manage Employees | ❌ | ❌ | ✅ |

---

## 🗄️ 7. Database Schema

### Users Table
- ID, Name, Email, Password Hash
- Role (ADMIN/EMPLOYEE/CUSTOMER)
- Balance, Total Transactions
- Created At, Last Login

### Transactions Table
- ID, User ID, Amount, Type
- Description, Timestamp

### Loans Table
- ID, User ID, Loan Type
- Amount, Tenure (months), Interest Rate
- Status, Purpose
- Application Date, Approval Date
- Approved By, Admin Comments

### Accounts Table
- ID, User ID, Balance

---

## 🚀 8. How to Use

### For Customers:
1. **Sign Up** → Create account with email/password
2. **Login** → Get authenticated
3. **Deposit** → Add money to account
4. **Transfer** → Send money to other users
5. **Apply for Loan** → Choose type, amount, tenure
6. **Track Loan** → View status and comments

### For Employees:
1. **Login** with employee credentials
2. **Search Customers** → Find by email
3. **Process Transactions** → Help customers deposit/withdraw
4. **Review Loans** → View pending applications
5. **Approve/Reject** → Make loan decisions with comments

### For Admins:
1. **Login** with admin credentials
2. **Manage Users** → Create employees, view all users
3. **System Overview** → View statistics
4. **Loan Management** → Full control over all loans
5. **Employee Management** → Create and manage employees

---

## 📝 9. Pre-Seeded Test Accounts

The application comes with test accounts:

| Email | Password | Role | Balance |
|-------|----------|------|---------|
| admin@baahubali.com | admin123 | ADMIN | $0.00 |
| employee@baahubali.com | employee123 | EMPLOYEE | $0.00 |
| alice@baahubali.com | password | CUSTOMER | $1000.00 |
| bob@baahubali.com | password | CUSTOMER | $500.00 |

---

## ✅ 10. Summary of Implementation Status

### ✅ Fully Implemented:
- Login/Sign Up system
- Deposit money
- Withdraw money
- Transfer within same bank (by email)
- Transaction history
- Four types of loans with different interest rates
- Loan application system
- Loan approval/rejection by Admin/Employee
- Auto-disbursement on approval
- Role-based dashboards (Customer, Employee, Admin)
- Complete access control
- User management (Admin)
- Employee management (Admin)
- System statistics

### ⚠️ Not Implemented:
- Transfer to other banks (requires external integration)
- Loan repayment system
- Interest calculation over time
- Loan EMI calculator
- Email notifications
- SMS alerts
- Account statements/reports

---

## 🎯 11. Key Security Features

- ✅ Password hashing (BCrypt)
- ✅ Token-based authentication
- ✅ Role-based authorization
- ✅ Balance validation (prevent overdraft)
- ✅ Self-transfer prevention
- ✅ Transaction atomicity
- ✅ Input validation

---

## 🌐 12. API Endpoints Summary

### Authentication
- `POST /api/auth/register` - Sign up
- `POST /api/auth/login` - Sign in
- `GET /api/auth/role?email={email}` - Get user role

### Transactions
- `POST /api/transactions` - Deposit/Withdraw
- `POST /api/transactions/transfer-by-email` - Transfer
- `GET /api/transactions/my-transactions` - View history

### Loans
- `POST /api/loans/apply` - Apply for loan
- `GET /api/loans/my-loans` - View my loans
- `GET /api/loans/pending` - View pending (Admin/Employee)
- `GET /api/loans/all` - View all (Admin/Employee)
- `POST /api/loans/{id}/approve` - Approve loan
- `POST /api/loans/{id}/reject` - Reject loan
- `GET /api/loans/types` - Get loan types

### Users (Admin)
- `GET /api/users` - List all users
- `POST /api/users` - Create user
- `GET /api/users/by-email/{email}` - Find by email
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Accounts
- `GET /api/accounts/me` - Get my account info

---

## 🎨 13. Frontend Features

The application includes a modern, responsive web interface with:
- Glassmorphism design
- Role-specific dashboards
- Real-time balance updates
- Transaction modals
- Loan application forms
- Admin/Employee management panels
- Mobile-responsive layout

---

**Built with**: Spring Boot 3.5.0, Java 21, H2 Database, Spring Security, JPA/Hibernate