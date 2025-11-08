package com.bank.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionDTO(String id, String userId, BigDecimal amount, String type, String description, Instant timestamp) {}


