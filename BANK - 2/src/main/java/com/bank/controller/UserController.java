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

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public UserController(UserRepository userRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    private boolean isAdmin(String token) {
        var rec = jwtUtils.validate(token);
        return rec != null && "ADMIN".equals(rec.role());
    }

    @GetMapping
    public ResponseEntity<?> list(@RequestHeader(name = "X-Auth-Token", required = false) String token) {
        if (!isAdmin(token)) return ResponseEntity.status(403).body(Map.of("error", "Admin only"));
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/by-email/{email}")
    public ResponseEntity<?> findByEmail(@RequestHeader(name = "X-Auth-Token", required = false) String token,
                                        @PathVariable String email) {
        var rec = jwtUtils.validate(token);
        if (rec == null) return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        
        return userRepository.findByEmail(email)
                .<ResponseEntity<?>>map(user -> ResponseEntity.ok(Map.of(
                    "id", user.getId(),
                    "name", user.getName(),
                    "email", user.getEmail()
                )))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestHeader(name = "X-Auth-Token", required = false) String token,
                                    @RequestBody CreateRequest request) {
        if (!isAdmin(token)) return ResponseEntity.status(403).body(Map.of("error", "Admin only"));
        if (userRepository.findByEmail(request.email()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already registered"));
        }
        User u = new User();
        u.setName(request.name());
        u.setEmail(request.email());
        u.setPasswordHash(PasswordEncoderUtil.encoder().encode(request.passwordHash()));
        u.setRole(request.role());
        u.setBalance(new java.math.BigDecimal("0.00"));
        return ResponseEntity.ok(userRepository.save(u));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestHeader(name = "X-Auth-Token", required = false) String token,
                                    @PathVariable String id,
                                    @RequestBody UpdateRequest request) {
        if (!isAdmin(token)) return ResponseEntity.status(403).body(Map.of("error", "Admin only"));
        return userRepository.findById(id).map(u -> {
            if (request.name() != null) u.setName(request.name());
            if (request.role() != null) u.setRole(request.role());
            return ResponseEntity.ok(userRepository.save(u));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@RequestHeader(name = "X-Auth-Token", required = false) String token,
                                    @PathVariable String id) {
        if (!isAdmin(token)) return ResponseEntity.status(403).body(Map.of("error", "Admin only"));
        if (!userRepository.existsById(id)) return ResponseEntity.notFound().build();
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public record CreateRequest(@NotBlank String name, @Email String email, @NotBlank String passwordHash, Role role) {}
    public record UpdateRequest(String name, Role role) {}
}


