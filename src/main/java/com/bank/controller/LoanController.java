package com.bank.controller;

import com.bank.entity.Loan;
import com.bank.loan.LoanType;
import com.bank.service.LoanService;
import com.bank.util.JwtUtils;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/loans")
@Validated
public class LoanController {
    
    private final LoanService loanService;
    private final JwtUtils jwtUtils;
    
    public LoanController(LoanService loanService, JwtUtils jwtUtils) {
        this.loanService = loanService;
        this.jwtUtils = jwtUtils;
    }
    
    private boolean isAdmin(String token) {
        var rec = jwtUtils.validate(token);
        return rec != null && "ADMIN".equals(rec.role());
    }
    
    private boolean isAdminOrEmployee(String token) {
        var rec = jwtUtils.validate(token);
        return rec != null && ("ADMIN".equals(rec.role()) || "EMPLOYEE".equals(rec.role()));
    }
    
    private String getUserId(String token) {
        var rec = jwtUtils.validate(token);
        if (rec == null) throw new RuntimeException("Invalid token");
        return rec.userId();
    }
    
    @PostMapping("/apply")
    public ResponseEntity<?> applyForLoan(@RequestHeader("X-Auth-Token") String token,
                                         @RequestBody LoanApplicationRequest request) {
        try {
            String userId = getUserId(token);
            Loan loan = loanService.applyForLoan(
                userId, 
                request.loanType(), 
                request.amount(), 
                request.tenureMonths(), 
                request.purpose()
            );
            return ResponseEntity.ok(Map.of(
                "message", "Loan application submitted successfully",
                "loanId", loan.getId(),
                "status", loan.getStatus().name()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/my-loans")
    public ResponseEntity<?> getMyLoans(@RequestHeader("X-Auth-Token") String token) {
        try {
            String userId = getUserId(token);
            List<Loan> loans = loanService.getUserLoans(userId);
            return ResponseEntity.ok(loans);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/pending")
    public ResponseEntity<?> getPendingLoans(@RequestHeader("X-Auth-Token") String token) {
        if (!isAdminOrEmployee(token)) {
            return ResponseEntity.status(403).body(Map.of("error", "Admin or Employee access required"));
        }
        
        try {
            List<Loan> loans = loanService.getPendingLoans();
            return ResponseEntity.ok(loans);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/all")
    public ResponseEntity<?> getAllLoans(@RequestHeader("X-Auth-Token") String token) {
        if (!isAdminOrEmployee(token)) {
            return ResponseEntity.status(403).body(Map.of("error", "Admin or Employee access required"));
        }
        
        try {
            List<Loan> loans = loanService.getAllLoans();
            return ResponseEntity.ok(loans);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/{loanId}/approve")
    public ResponseEntity<?> approveLoan(@RequestHeader("X-Auth-Token") String token,
                                         @PathVariable String loanId,
                                         @RequestBody LoanDecisionRequest request) {
        if (!isAdmin(token)) {
            return ResponseEntity.status(403).body(Map.of("error", "Admin access required"));
        }
        
        try {
            String adminId = getUserId(token);
            Loan loan = loanService.approveLoan(loanId, adminId, request.comments());
            
            return ResponseEntity.ok(Map.of(
                "message", "Loan approved successfully",
                "loanId", loan.getId(),
                "status", loan.getStatus().name()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/{loanId}/reject")
    public ResponseEntity<?> rejectLoan(@RequestHeader("X-Auth-Token") String token,
                                        @PathVariable String loanId,
                                        @RequestBody LoanDecisionRequest request) {
        if (!isAdmin(token)) {
            return ResponseEntity.status(403).body(Map.of("error", "Admin access required"));
        }
        
        try {
            String adminId = getUserId(token);
            Loan loan = loanService.rejectLoan(loanId, adminId, request.comments());
            return ResponseEntity.ok(Map.of(
                "message", "Loan rejected",
                "loanId", loan.getId(),
                "status", loan.getStatus().name()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/types")
    public ResponseEntity<?> getLoanTypes() {
        LoanType[] types = LoanType.values();
        return ResponseEntity.ok(types);
    }
    
    
    public record LoanApplicationRequest(
        @NotNull LoanType loanType,
        @NotNull @DecimalMin("1000.0") BigDecimal amount,
        @NotNull @Min(1) Integer tenureMonths,
        @NotBlank String purpose
    ) {}
    
    public record LoanDecisionRequest(String comments) {}
}
