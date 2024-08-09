package com.eduquiz.service;

import com.eduquiz.model.User;
import com.eduquiz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return "Username already taken";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User registered successfully";
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public void testPasswordEncoding() {
        String rawPassword = "password123";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);

        System.out.println("Raw Password: " + rawPassword);
        System.out.println("Encoded Password: " + encodedPassword);
        System.out.println("Password matches: " + matches);
    }

}
