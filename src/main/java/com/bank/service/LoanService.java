package com.bank.service;

import com.bank.entity.Loan;
import com.bank.entity.User;
import com.bank.loan.LoanStatus;
import com.bank.loan.LoanType;
import com.bank.repository.LoanRepository;
import com.bank.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {
    
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final com.bank.repository.AccountRepository accountRepository;

    public LoanService(LoanRepository loanRepository, UserRepository userRepository, com.bank.repository.AccountRepository accountRepository) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }
    
    public Loan applyForLoan(String userId, LoanType loanType, BigDecimal amount, 
                             Integer tenureMonths, String purpose) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Use default interest rate for the loan type
        BigDecimal interestRate = BigDecimal.valueOf(loanType.getDefaultInterestRate());
        
        Loan loan = new Loan(user, loanType, amount, tenureMonths, interestRate, purpose);
        return loanRepository.save(loan);
    }
    
    public List<Loan> getUserLoans(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return loanRepository.findByUserOrderByApplicationDateDesc(user);
    }
    
    public List<Loan> getPendingLoans() {
        return loanRepository.findByStatusOrderByApplicationDateDesc(LoanStatus.PENDING);
    }
    
    public List<Loan> getAllLoans() {
        return loanRepository.findAllByOrderByApplicationDateDesc();
    }
    
    public Loan approveLoan(String loanId, String adminId, String comments) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
        
        // Defensive fixes for potential legacy nulls
        if (loan.getApplicationDate() == null) loan.setApplicationDate(Instant.now());
        if (loan.getStatus() == null) loan.setStatus(LoanStatus.PENDING);
        if (loan.getInterestRate() == null) {
            BigDecimal rate = loan.getLoanType() != null ? BigDecimal.valueOf(loan.getLoanType().getDefaultInterestRate()) : BigDecimal.valueOf(10.0);
            loan.setInterestRate(rate);
        }
        
        if (loan.getStatus() != LoanStatus.PENDING) {
            throw new RuntimeException("Loan is not in pending status");
        }
        
        loan.setStatus(LoanStatus.APPROVED);
        loan.setApprovalDate(Instant.now());
        loan.setApprovedBy(adminId);
        loan.setAdminComments(comments);
        
        Loan savedLoan = loanRepository.save(loan);


        // Credit loan amount to user's account and update user balance
        String userId = loan.getUser().getId();
        BigDecimal loanAmount = loan.getAmount();

        // Update Account balance, create if missing
        accountRepository.findByUserId(userId).ifPresentOrElse(account -> {
            account.setBalance(account.getBalance().add(loanAmount));
            accountRepository.save(account);
        }, () -> {
            com.bank.entity.Account newAccount = new com.bank.entity.Account();
            newAccount.setUserId(userId);
            newAccount.setBalance(loanAmount);
            accountRepository.save(newAccount);
        });

        // Update User balance
        User user = loan.getUser();
        user.setBalance(user.getBalance().add(loanAmount));
        userRepository.save(user);

        return savedLoan;
    }
    
    public Loan rejectLoan(String loanId, String adminId, String comments) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
        
        // Defensive fixes for potential legacy nulls
        if (loan.getApplicationDate() == null) loan.setApplicationDate(Instant.now());
        if (loan.getStatus() == null) loan.setStatus(LoanStatus.PENDING);
        if (loan.getInterestRate() == null) {
            BigDecimal rate = loan.getLoanType() != null ? BigDecimal.valueOf(loan.getLoanType().getDefaultInterestRate()) : BigDecimal.valueOf(10.0);
            loan.setInterestRate(rate);
        }
        
        if (loan.getStatus() != LoanStatus.PENDING) {
            throw new RuntimeException("Loan is not in pending status");
        }
        
        loan.setStatus(LoanStatus.REJECTED);
        loan.setApprovalDate(Instant.now());
        loan.setApprovedBy(adminId);
        loan.setAdminComments(comments);
        
        return loanRepository.save(loan);
    }
    
    public Optional<Loan> getLoanById(String loanId) {
        return loanRepository.findById(loanId);
    }
}
