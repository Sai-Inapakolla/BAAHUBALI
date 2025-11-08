# BAAHUBALI Bank - Data Persistence Guide

## 🗄️ Database Configuration

Your BAAHUBALI Bank now uses **persistent file-based storage** instead of in-memory storage.

### Database Location
- **File Path**: `./data/baahubali-bank.mv.db`
- **Type**: H2 File Database
- **Persistence**: Data survives application restarts

### What This Means
✅ **Data Persists**: All users, accounts, and transactions are saved permanently
✅ **No Data Loss**: Restarting the application won't lose your data
✅ **Real Banking**: Works like a real-world banking application

## 🚀 How to Use

### 1. First Time Setup
```bash
mvn spring-boot:run
```
- Creates initial admin and test users
- Database file is created in `./data/` folder

### 2. Test Data Persistence
1. **Register a new user** or **login with existing users**:
   - `admin@baahubali.com` / `admin123` (Admin)
   - `alice@baahubali.com` / `password` (Customer, $1000)
   - `bob@baahubali.com` / `password` (Customer, $500)

2. **Perform transactions**:
   - Deposit money
   - Withdraw money
   - Transfer between users

3. **Stop the application** (Ctrl+C)

4. **Restart the application**:
   ```bash
   mvn spring-boot:run
   ```

5. **Login again** - All your data will be there!

## 📁 Database Files

After first run, you'll see:
```
D:\JAVA\BANK - 2\
├── data\
│   ├── baahubali-bank.mv.db      # Main database file
│   └── baahubali-bank.trace.db   # Trace file
└── ...
```

## 🔧 Database Console

Access H2 Console at: `http://localhost:8080/h2-console`

**Connection Details:**
- **JDBC URL**: `jdbc:h2:file:./data/baahubali-bank`
- **Username**: `sa`
- **Password**: (leave empty)

## ⚠️ Important Notes

1. **Don't delete** the `./data/` folder - it contains your bank data
2. **Backup** the `./data/` folder to preserve your data
3. **Database grows** as you add more transactions
4. **Data is permanent** - only deleted if you manually delete the files

## 🎯 Benefits

- ✅ **Real-world behavior**: Data persists like actual banking systems
- ✅ **No re-registration**: Users don't need to register again
- ✅ **Transaction history**: All transactions are permanently stored
- ✅ **Account balances**: Balances are maintained across restarts
- ✅ **Professional**: Works like a production banking application

Your BAAHUBALI Bank now behaves like a real banking application! 🏦✨
