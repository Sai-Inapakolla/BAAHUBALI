# 🚀 Quick Start Guide - BAAHUBALI Bank

## Get Started in 3 Minutes!

---

## Step 1: Start the Application

### Option A: Using the Batch File (Windows)
```bash
# Double-click or run:
start-bank.bat
```

### Option B: Using Maven
```bash
mvn spring-boot:run
```

### Option C: Using JAR
```bash
mvn clean package
java -jar target/bank-mvp-0.0.1-SNAPSHOT.jar
```

---

## Step 2: Access the Application

Open your browser and go to:
```
http://localhost:8080
```

---

## Step 3: Login with Test Account

### Try as Customer (Alice)
```
Email: alice@baahubali.com
Password: password
Initial Balance: $1,000.00
```

### Try as Employee
```
Email: employee@baahubali.com
Password: employee123
```

### Try as Admin
```
Email: admin@baahubali.com
Password: admin123
```

---

## 🎯 What to Try First

### As Customer (Alice):
1. ✅ **View your balance** - See $1,000.00
2. ✅ **Deposit $500** - Watch balance increase
3. ✅ **Transfer $200 to Bob** - Send to: bob@baahubali.com
4. ✅ **Apply for Educational Loan** - $5,000 for 24 months
5. ✅ **Check transaction history** - See all your transactions

### As Admin:
1. ✅ **View pending loans** - See Alice's loan application
2. ✅ **Approve the loan** - Add comment and approve
3. ✅ **Check system stats** - View total users and balances
4. ✅ **Create new user** - Add a new customer or employee

### As Employee:
1. ✅ **Search for customer** - Find alice@baahubali.com
2. ✅ **Process deposit** - Help customer deposit money
3. ✅ **Review loans** - Approve or reject applications

---

## 📋 Complete Test Flow

### 5-Minute Complete Test

**1. Customer Operations (2 min)**
- Login as Alice
- Deposit $500
- Transfer $300 to Bob
- Apply for Personal Loan ($3,000)
- Logout

**2. Admin Operations (2 min)**
- Login as Admin
- View pending loans
- Approve Alice's loan
- Check Alice's new balance (should be increased by $3,000)
- View system statistics

**3. Verification (1 min)**
- Login as Alice again
- Check balance (should show loan amount added)
- View "My Loans" - see approved status
- Check transaction history

---

## 🎨 Features You Can Test

### Banking Operations
- [x] Deposit money
- [x] Withdraw money
- [x] Transfer to other users (by email)
- [x] View transaction history

### Loan Management
- [x] Apply for loan (4 types available)
- [x] View loan status
- [x] See approval/rejection comments
- [x] Track loan history

### Admin Features
- [x] View all users
- [x] Create new users
- [x] Approve/reject loans
- [x] View system statistics
- [x] Manage employees

---

## 🔍 Database Access (Optional)

Want to see the data directly?

```
URL: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:file:./data/baahubali-bank
Username: sa
Password: (leave empty)
```

**Tables to explore:**
- `USERS` - All user accounts
- `TRANSACTIONS` - All transactions
- `LOANS` - All loan applications
- `ACCOUNTS` - Account balances

---

## 🐛 Troubleshooting

### Application won't start?
```bash
# Check if port 8080 is already in use
netstat -ano | findstr :8080

# Kill the process if needed
taskkill /PID <process_id> /F

# Try again
mvn spring-boot:run
```

### Can't login?
- Check email spelling (case-sensitive)
- Verify password is correct
- Try with test accounts listed above

### Balance not updating?
- Refresh the page (F5)
- Logout and login again
- Check transaction history to verify

---

## 📚 Need More Help?

Check these documents:
- **[README.md](README.md)** - Complete overview
- **[BANK_APPLICATION_FEATURES.md](BANK_APPLICATION_FEATURES.md)** - All features
- **[TESTING_GUIDE.md](TESTING_GUIDE.md)** - Detailed testing scenarios
- **[FEATURE_CHECKLIST.md](FEATURE_CHECKLIST.md)** - Implementation status

---

## 🎉 You're All Set!

Your BAAHUBALI Bank application is ready to use. Enjoy exploring all the features!

**Happy Banking! 🏦**