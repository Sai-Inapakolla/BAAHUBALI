package com.bank.controller;

import com.bank.repository.UserRepository;
import com.bank.util.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public AccountController(UserRepository userRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/me")
    public ResponseEntity<?> myAccount(@RequestHeader(name = "X-Auth-Token", required = false) String token) {
        var rec = jwtUtils.validate(token);
        if (rec == null) return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        
        // Return user data with balance (since we're using User.balance, not Account.balance)
        return userRepository.findById(rec.userId())
                .<ResponseEntity<?>>map(user -> ResponseEntity.ok(Map.of(
                    "id", user.getId(),
                    "balance", user.getBalance(),
                    "user", Map.of(
                        "id", user.getId(),
                        "name", user.getName(),
                        "email", user.getEmail(),
                        "role", user.getRole().name()
                    )
                )))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}


