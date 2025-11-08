package com.bank.security;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SimpleTokenService {

    private static final long DEFAULT_TTL_SECONDS = 60L * 60L * 8L; // 8 hours

    private final Map<String, TokenRecord> tokenToRecord = new ConcurrentHashMap<>();

    public String issueToken(String userId, String role) {
        String token = UUID.randomUUID().toString();
        tokenToRecord.put(token, new TokenRecord(userId, role, Instant.now().plusSeconds(DEFAULT_TTL_SECONDS)));
        return token;
    }

    public TokenRecord validate(String token) {
        TokenRecord record = tokenToRecord.get(token);
        if (record == null) return null;
        if (Instant.now().isAfter(record.expiresAt())) {
            tokenToRecord.remove(token);
            return null;
        }
        return record;
    }

    public void revoke(String token) {
        tokenToRecord.remove(token);
    }

    public record TokenRecord(String userId, String role, Instant expiresAt) {}
}


