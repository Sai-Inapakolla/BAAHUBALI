package com.bank.bootstrap;

import com.bank.entity.Transaction;
import com.bank.repository.TransactionRepository;
import com.bank.transaction.TransactionType;
import com.bank.user.Role;
import com.bank.entity.User;
import com.bank.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.bank.util.PasswordEncoderUtil;

import java.math.BigDecimal;

@Configuration
public class SeedData {

    @Bean
    public CommandLineRunner seed(UserRepository users, TransactionRepository txs) {
        return args -> {
            if (users.count() > 0) return;
            var encoder = PasswordEncoderUtil.encoder();

            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@baahubali.com");
            admin.setPasswordHash(encoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            admin.setBalance(new BigDecimal("0.00"));
            users.save(admin);

            User alice = new User();
            alice.setName("Alice");
            alice.setEmail("alice@baahubali.com");
            alice.setPasswordHash(encoder.encode("password"));
            alice.setRole(Role.CUSTOMER);
            alice.setBalance(new BigDecimal("1000.00"));
            users.save(alice);

            User bob = new User();
            bob.setName("Bob");
            bob.setEmail("bob@baahubali.com");
            bob.setPasswordHash(encoder.encode("password"));
            bob.setRole(Role.CUSTOMER);
            bob.setBalance(new BigDecimal("500.00"));
            users.save(bob);

            User employee = new User();
            employee.setName("Employee");
            employee.setEmail("employee@baahubali.com");
            employee.setPasswordHash(encoder.encode("employee123"));
            employee.setRole(Role.EMPLOYEE);
            employee.setBalance(new BigDecimal("0.00"));
            users.save(employee);

            Transaction t1 = new Transaction();
            t1.setUserId(alice.getId());
            t1.setAmount(new BigDecimal("200.00"));
            t1.setType(TransactionType.DEPOSIT);
            t1.setDescription("Initial deposit");
            txs.save(t1);

            Transaction t2 = new Transaction();
            t2.setUserId(bob.getId());
            t2.setAmount(new BigDecimal("50.00"));
            t2.setType(TransactionType.WITHDRAW);
            t2.setDescription("ATM withdrawal");
            txs.save(t2);
        };
    }
}


