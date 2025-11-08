package com.bank.service;

import com.bank.entity.User;
import com.bank.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) { this.userRepository = userRepository; }
    public List<User> findAll() { return userRepository.findAll(); }
    public Optional<User> findById(String id) { return userRepository.findById(id); }
    public Optional<User> findByEmail(String email) { return userRepository.findByEmail(email); }
    public User save(User user) { return userRepository.save(user); }
    public boolean existsById(String id) { return userRepository.existsById(id); }
    public void deleteById(String id) { userRepository.deleteById(id); }
}


