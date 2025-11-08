package com.bank.entity;

import com.bank.loan.LoanStatus;
import com.bank.loan.LoanType;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import java.math.BigDecimal;
import java.time.Instant;

@Document(collection = "loans")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Loan {
    
    @Id
    private String id;
    
    @DBRef
    private User user;
    
    private LoanType loanType;
    
    private BigDecimal amount;
    
    private Integer tenureMonths;
    
    private BigDecimal interestRate;
    
    private LoanStatus status;
    
    private String purpose;
    
    private String adminComments;
    
    private Instant applicationDate;
    
    private Instant approvalDate;
    
    private String approvedBy;
    
    // Constructors
    public Loan() {}
    
    public Loan(User user, LoanType loanType, BigDecimal amount, Integer tenureMonths, 
                BigDecimal interestRate, String purpose) {
        this.user = user;
        this.loanType = loanType;
        this.amount = amount;
        this.tenureMonths = tenureMonths;
        this.interestRate = interestRate;
        this.purpose = purpose;
        this.status = LoanStatus.PENDING;
        this.applicationDate = Instant.now();
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public LoanType getLoanType() {
        return loanType;
    }
    
    public void setLoanType(LoanType loanType) {
        this.loanType = loanType;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public Integer getTenureMonths() {
        return tenureMonths;
    }
    
    public void setTenureMonths(Integer tenureMonths) {
        this.tenureMonths = tenureMonths;
    }
    
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    
    public LoanStatus getStatus() {
        return status;
    }
    
    public void setStatus(LoanStatus status) {
        this.status = status;
    }
    
    public String getPurpose() {
        return purpose;
    }
    
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    
    public String getAdminComments() {
        return adminComments;
    }
    
    public void setAdminComments(String adminComments) {
        this.adminComments = adminComments;
    }
    
    public Instant getApplicationDate() {
        return applicationDate;
    }
    
    public void setApplicationDate(Instant applicationDate) {
        this.applicationDate = applicationDate;
    }
    
    public Instant getApprovalDate() {
        return approvalDate;
    }
    
    public void setApprovalDate(Instant approvalDate) {
        this.approvalDate = approvalDate;
    }
    
    public String getApprovedBy() {
        return approvedBy;
    }
    
    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }
}
