package com.bank.controller;

import com.bank.entity.User;
import com.bank.repository.UserRepository;
import com.bank.user.Role;
import com.bank.util.JwtUtils;
import com.bank.util.PasswordEncoderUtil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public AuthController(UserRepository userRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/role")
    public ResponseEntity<?> getRoleByEmail(@RequestParam("email") String email) {
        return userRepository.findByEmail(email)
                .<ResponseEntity<?>>map(user -> ResponseEntity.ok(Map.of(
                        "role", user.getRole().name()
                )))
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "User not found")));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already registered"));
        }
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPasswordHash(PasswordEncoderUtil.encoder().encode(request.password()));
        user.setRole(Role.CUSTOMER);
        user = userRepository.save(user);
        String token = jwtUtils.issueToken(user.getId(), user.getRole().name());
        return ResponseEntity.ok(Map.of(
                "token", token,
                "userId", user.getId(),
                "role", user.getRole().name()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return userRepository.findByEmail(request.email())
                .filter(u -> PasswordEncoderUtil.encoder().matches(request.password(), u.getPasswordHash()))
                .map(u -> {
                    u.setLastLogin(Instant.now());
                    userRepository.save(u);
                    String token = jwtUtils.issueToken(u.getId(), u.getRole().name());
                    return ResponseEntity.ok(Map.of(
                            "token", token,
                            "userId", u.getId(),
                            "role", u.getRole().name(),
                            "name", u.getName()
                    ));
                })
                .orElseGet(() -> ResponseEntity.status(401).body(Map.of("error", "Invalid credentials")));
    }

    public record RegisterRequest(@NotBlank String name, @Email String email, @NotBlank String password) {}
    public record LoginRequest(@Email String email, @NotBlank String password) {}
}


