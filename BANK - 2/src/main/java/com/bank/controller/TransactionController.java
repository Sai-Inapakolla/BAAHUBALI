package com.bank.controller;

import com.bank.entity.Transaction;
import com.bank.repository.TransactionRepository;
import com.bank.transaction.TransactionType;
import com.bank.entity.User;
import com.bank.repository.UserRepository;
import com.bank.util.JwtUtils;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@Validated
public class TransactionController {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public TransactionController(TransactionRepository transactionRepository, UserRepository userRepository, JwtUtils jwtUtils) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    private String requireUser(String token) {
        var rec = jwtUtils.validate(token);
        return rec != null ? rec.userId() : null;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> history(@RequestHeader(name = "X-Auth-Token", required = false) String token,
                                     @PathVariable String id) {
        var rec = jwtUtils.validate(token);
        if (rec == null) return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        if (!rec.userId().equals(id) && !"ADMIN".equals(rec.role()))
            return ResponseEntity.status(403).body(Map.of("error", "Forbidden"));
        List<Transaction> txs = transactionRepository.findByUserIdOrderByTimestampDesc(id);
        return ResponseEntity.ok(txs);
    }

    @GetMapping("/my-transactions")
    public ResponseEntity<?> myTransactions(@RequestHeader(name = "X-Auth-Token", required = false) String token) {
        String userId = requireUser(token);
        if (userId == null) return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        
        List<Transaction> txs = transactionRepository.findByUserIdOrderByTimestampDesc(userId);
        return ResponseEntity.ok(txs);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestHeader(name = "X-Auth-Token", required = false) String token,
                                    @RequestBody CreateRequest request) {
        String userId = requireUser(token);
        if (userId == null) return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        if (!(request.type() == TransactionType.DEPOSIT || request.type() == TransactionType.WITHDRAW)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Unsupported type for this endpoint"));
        }
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return ResponseEntity.status(404).body(Map.of("error", "User not found"));

        BigDecimal newBalance = user.getBalance();
        if (request.type() == TransactionType.DEPOSIT) {
            newBalance = newBalance.add(request.amount());
        } else {
            if (newBalance.compareTo(request.amount()) < 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Insufficient funds"));
            }
            newBalance = newBalance.subtract(request.amount());
        }
        user.setBalance(newBalance);
        user.setTotalTransactions(user.getTotalTransactions() + 1);
        userRepository.save(user);

        Transaction tx = new Transaction();
        tx.setUserId(userId);
        tx.setAmount(request.amount());
        tx.setType(request.type());
        tx.setDescription(request.description() != null && !request.description().trim().isEmpty() 
            ? request.description() 
            : (request.type() == TransactionType.DEPOSIT ? "Money deposit" : "Money withdrawal"));
        transactionRepository.save(tx);

        return ResponseEntity.ok(Map.of("balance", user.getBalance()));
    }

    @PostMapping("/transfer")
    @Transactional
    public ResponseEntity<?> transfer(@RequestHeader(name = "X-Auth-Token", required = false) String token,
                                      @RequestBody TransferRequest request) {
        String fromId = requireUser(token);
        if (fromId == null) return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        if (fromId.equals(request.toUserId())) return ResponseEntity.badRequest().body(Map.of("error", "Cannot transfer to self"));

        User from = userRepository.findById(fromId).orElse(null);
        User to = userRepository.findById(request.toUserId()).orElse(null);
        if (from == null || to == null) return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        if (from.getBalance().compareTo(request.amount()) < 0) return ResponseEntity.badRequest().body(Map.of("error", "Insufficient funds"));

        from.setBalance(from.getBalance().subtract(request.amount()));
        to.setBalance(to.getBalance().add(request.amount()));
        from.setTotalTransactions(from.getTotalTransactions() + 1);
        to.setTotalTransactions(to.getTotalTransactions() + 1);
        userRepository.save(from);
        userRepository.save(to);

        Transaction debit = new Transaction();
        debit.setUserId(fromId);
        debit.setAmount(request.amount());
        debit.setType(TransactionType.TRANSFER);
        debit.setDescription("Transfer to " + to.getName() + " (" + to.getEmail() + ")");
        transactionRepository.save(debit);

        Transaction credit = new Transaction();
        credit.setUserId(to.getId());
        credit.setAmount(request.amount());
        credit.setType(TransactionType.TRANSFER);
        credit.setDescription("Transfer from " + from.getName() + " (" + from.getEmail() + ")");
        transactionRepository.save(credit);

        return ResponseEntity.ok(Map.of(
            "fromBalance", from.getBalance(), 
            "toBalance", to.getBalance(),
            "message", "Transfer successful"
        ));
    }

    @PostMapping("/transfer-by-email")
    @Transactional
    public ResponseEntity<?> transferByEmail(@RequestHeader(name = "X-Auth-Token", required = false) String token,
                                           @RequestBody TransferByEmailRequest request) {
        String fromId = requireUser(token);
        if (fromId == null) return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));

        User from = userRepository.findById(fromId).orElse(null);
        if (from == null) return ResponseEntity.status(404).body(Map.of("error", "Sender not found"));

        User to = userRepository.findByEmail(request.toEmail()).orElse(null);
        if (to == null) return ResponseEntity.status(404).body(Map.of("error", "Recipient not found"));
        
        if (fromId.equals(to.getId())) return ResponseEntity.badRequest().body(Map.of("error", "Cannot transfer to self"));
        if (from.getBalance().compareTo(request.amount()) < 0) return ResponseEntity.badRequest().body(Map.of("error", "Insufficient funds"));

        // Update balances
        from.setBalance(from.getBalance().subtract(request.amount()));
        to.setBalance(to.getBalance().add(request.amount()));
        from.setTotalTransactions(from.getTotalTransactions() + 1);
        to.setTotalTransactions(to.getTotalTransactions() + 1);
        userRepository.save(from);
        userRepository.save(to);

        // Create transaction records
        Transaction debit = new Transaction();
        debit.setUserId(fromId);
        debit.setAmount(request.amount());
        debit.setType(TransactionType.TRANSFER);
        debit.setDescription("Transfer to " + to.getName() + " (" + to.getEmail() + ")");
        transactionRepository.save(debit);

        Transaction credit = new Transaction();
        credit.setUserId(to.getId());
        credit.setAmount(request.amount());
        credit.setType(TransactionType.TRANSFER);
        credit.setDescription("Transfer from " + from.getName() + " (" + from.getEmail() + ")");
        transactionRepository.save(credit);

        return ResponseEntity.ok(Map.of(
            "fromBalance", from.getBalance(), 
            "toBalance", to.getBalance(),
            "message", "Transfer successful to " + to.getName()
        ));
    }

    public record CreateRequest(@NotNull BigDecimal amount, @NotNull TransactionType type, String description) {}
    public record TransferRequest(@NotNull String toUserId, @NotNull BigDecimal amount) {}
    public record TransferByEmailRequest(@NotNull String toEmail, @NotNull BigDecimal amount) {}
}


