package com.bank.repository;

import com.bank.entity.Loan;
import com.bank.entity.User;
import com.bank.loan.LoanStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends MongoRepository<Loan, String> {
    
    List<Loan> findByUserOrderByApplicationDateDesc(User user);
    
    List<Loan> findByStatusOrderByApplicationDateDesc(LoanStatus status);
    
    List<Loan> findAllByOrderByApplicationDateDesc();
    
    long countByUserAndStatus(User user, LoanStatus status);
}
