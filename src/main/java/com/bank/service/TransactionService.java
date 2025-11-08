package com.bank.service;

import com.bank.repository.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    public TransactionService(TransactionRepository transactionRepository) { this.transactionRepository = transactionRepository; }
    public long countAll() { return transactionRepository.count(); }
}


