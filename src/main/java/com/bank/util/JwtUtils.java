package com.bank.util;

import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    public static record TokenRecord(String userId, String role) {}

    public String issueToken(String userId, String role) {
        // NOTE: MVP placeholder; not a real JWT
        return userId + ":" + role;
    }

    public TokenRecord validate(String token) {
        if (token == null || !token.contains(":")) return null;
        try {
            String[] parts = token.split(":", 2);
            String uid = parts[0];
            return new TokenRecord(uid, parts[1]);
        } catch (Exception e) {
            return null;
        }
    }
}


