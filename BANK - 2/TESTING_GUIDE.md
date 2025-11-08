# 🧪 BAAHUBALI Bank - Testing Guide

## Quick Start Testing

### 1. Start the Application
```bash
mvn spring-boot:run
```
Access at: http://localhost:8080

---

## 🔐 Test Accounts

| Role | Email | Password | Initial Balance |
|------|-------|----------|----------------|
| **Admin** | admin@baahubali.com | admin123 | $0.00 |
| **Employee** | employee@baahubali.com | employee123 | $0.00 |
| **Customer 1** | alice@baahubali.com | password | $1,000.00 |
| **Customer 2** | bob@baahubali.com | password | $500.00 |

---

## 📋 Test Scenarios

### Scenario 1: Customer Banking Operations
**Login as Alice (Customer)**

1. ✅ **View Balance**: Should show $1,000.00
2. ✅ **Deposit Money**: 
   - Amount: $500
   - New Balance: $1,500.00
3. ✅ **Withdraw Money**:
   - Amount: $200
   - New Balance: $1,300.00
4. ✅ **Transfer to Bob**:
   - Recipient: bob@baahubali.com
   - Amount: $300
   - Alice's Balance: $1,000.00
   - Bob's Balance: $800.00
5. ✅ **View Transaction History**: Should show all transactions

---

### Scenario 2: Loan Application & Approval
**Step 1: Apply for Loan (as Alice)**

1. Click "Apply for Loan"
2. Select: **Educational Loan** (8.5% interest)
3. Amount: $5,000
4. Tenure: 24 months
5. Purpose: "College tuition fees"
6. Submit → Status: PENDING

**Step 2: Approve Loan (as Admin/Employee)**

1. Logout from Alice's account
2. Login as: admin@baahubali.com / admin123
3. Click "Pending Loans"
4. Find Alice's loan application
5. Click "Approve"
6. Add comment: "Approved based on good credit history"
7. Confirm → Status: APPROVED
8. **Alice's balance automatically increases by $5,000**

**Step 3: Verify (as Alice)**

1. Logout and login as Alice
2. Click "My Loans"
3. See approved loan with admin comments
4. Check balance: Should be increased by $5,000

---

### Scenario 3: Employee Operations
**Login as Employee**

1. ✅ **Search Customer**:
   - Enter: alice@baahubali.com
   - View customer details
2. ✅ **Process Deposit for Customer**:
   - Customer: alice@baahubali.com
   - Amount: $1,000
   - Description: "Cash deposit"
3. ✅ **Process Withdrawal for Customer**:
   - Customer: bob@baahubali.com
   - Amount: $100
   - Description: "ATM withdrawal"
4. ✅ **Review Pending Loans**:
   - View all pending applications
   - Approve or reject with comments

---

### Scenario 4: Admin Operations
**Login as Admin**

1. ✅ **View System Statistics**:
   - Total users
   - Total balance
   - Total transactions
   - Loan statistics
2. ✅ **Create New Employee**:
   - Name: "John Doe"
   - Email: john@baahubali.com
   - Password: employee123
   - Role: EMPLOYEE
3. ✅ **Create New Customer**:
   - Name: "Jane Smith"
   - Email: jane@baahubali.com
   - Password: password
   - Role: CUSTOMER
4. ✅ **View All Users**:
   - See breakdown by role
   - View balances
5. ✅ **Manage Employees**:
   - View all employees
   - Create new employees

---

### Scenario 5: Different Loan Types
**Test Each Loan Type**

1. **Educational Loan** (8.5%):
   - Amount: $10,000
   - Tenure: 48 months
   - Purpose: "MBA program"

2. **Farming Loan** (6.0%):
   - Amount: $20,000
   - Tenure: 36 months
   - Purpose: "Purchase farming equipment"

3. **Gold Loan** (12.0%):
   - Amount: $5,000
   - Tenure: 12 months
   - Purpose: "Emergency medical expenses"

4. **Personal Loan** (15.0%):
   - Amount: $3,000
   - Tenure: 24 months
   - Purpose: "Home renovation"

---

### Scenario 6: Loan Rejection
**As Admin/Employee**

1. View pending loans
2. Select a loan application
3. Click "Reject"
4. Enter reason: "Insufficient credit score"
5. Confirm
6. Verify status changed to REJECTED
7. Customer can see rejection reason

---

## 🚫 Error Testing

### Test Insufficient Funds
1. Login as Bob (balance: $500)
2. Try to withdraw $600
3. Should show error: "Insufficient funds"

### Test Self-Transfer
1. Try to transfer money to your own email
2. Should show error: "Cannot transfer to self"

### Test Invalid Recipient
1. Try to transfer to non-existent email
2. Should show error: "Recipient not found"

### Test Unauthorized Access
1. Login as Customer
2. Try to access admin features
3. Should show error: "Admin access required"

---

## 📊 Expected Results Summary

### After All Tests:
- **Alice**: Multiple transactions, at least one approved loan
- **Bob**: Received transfer from Alice
- **Admin**: Created new users, approved/rejected loans
- **Employee**: Processed customer transactions
- **System**: All transactions recorded, balances updated correctly

---

## 🔍 Verification Checklist

- [ ] All test accounts can login
- [ ] Deposits increase balance
- [ ] Withdrawals decrease balance
- [ ] Transfers work between users
- [ ] Transaction history shows all operations
- [ ] Loan applications create PENDING status
- [ ] Admin/Employee can approve loans
- [ ] Approved loans credit user balance
- [ ] Rejected loans show rejection reason
- [ ] Role-based access control works
- [ ] Error messages display correctly
- [ ] Balance validations prevent overdraft
- [ ] All dashboards display correct data

---

## 🐛 Common Issues & Solutions

### Issue: Cannot login
- **Solution**: Check email/password spelling
- **Solution**: Ensure application is running on port 8080

### Issue: Balance not updating
- **Solution**: Refresh the page
- **Solution**: Logout and login again

### Issue: Loan not appearing
- **Solution**: Check "My Loans" section
- **Solution**: Verify loan was submitted successfully

### Issue: Cannot approve loan
- **Solution**: Ensure logged in as Admin or Employee
- **Solution**: Check loan is in PENDING status

---

## 🎯 Test Coverage

✅ **Authentication**: Login, Logout, Role verification  
✅ **Transactions**: Deposit, Withdraw, Transfer  
✅ **Loans**: Apply, Approve, Reject, View  
✅ **User Management**: Create, View, Search  
✅ **Access Control**: Role-based permissions  
✅ **Error Handling**: Validation, Insufficient funds  
✅ **Balance Updates**: Real-time updates  
✅ **Transaction History**: Complete audit trail  

---

## 📱 UI Testing

### Desktop (1920x1080)
- [ ] All dashboards display correctly
- [ ] Modals open and close properly
- [ ] Forms submit successfully
- [ ] Tables are readable

### Tablet (768x1024)
- [ ] Responsive layout works
- [ ] Touch interactions work
- [ ] Navigation is accessible

### Mobile (375x667)
- [ ] Mobile-optimized layout
- [ ] All features accessible
- [ ] Forms are usable

---

## 🔄 Continuous Testing

### Daily Operations Test
1. Login as different roles
2. Perform 2-3 transactions each
3. Apply for 1 loan
4. Approve/Reject 1 loan
5. Verify all balances are correct

### Weekly Full Test
- Run all scenarios
- Verify data consistency
- Check system statistics
- Test error conditions

---

**Happy Testing! 🎉**