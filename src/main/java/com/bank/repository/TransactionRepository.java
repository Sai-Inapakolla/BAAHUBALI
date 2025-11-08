package com.bank.repository;

import com.bank.entity.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByUserIdOrderByTimestampDesc(String userId);
}


