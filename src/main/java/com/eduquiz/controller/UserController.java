package com.eduquiz.controller;

import com.eduquiz.model.User;
import com.eduquiz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("quizUser", new User());
        return "register";
    }

    @PostMapping("/submit_register")
    public String registerUser(@ModelAttribute("quizUser") User user, Model model) {
        String result = userService.registerUser(user);
        if (result.equals("User registered successfully")) {
            return "redirect:/api/users/login";
        } else {
            model.addAttribute("error", result);
            return "register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/submit_login")
    public String loginUser(@RequestParam("username") String username,
                            @RequestParam("password") String password,
                            Model model) {
        System.out.println("Attempting login for username: " + username);

        User user = userService.findByUsername(username);
        if (user != null) {
            System.out.println("User found: " + user.getUsername());
            boolean passwordMatch = passwordEncoder.matches(password, user.getPassword());
            System.out.println("Password match: " + passwordMatch);

            if (passwordMatch) {
                System.out.println("Login successful, redirecting to /home");
                return "home";
            }
        }

        System.out.println("Login failed, invalid credentials");
        model.addAttribute("error", "Invalid username or password");
        return "login";
    }




    @GetMapping("/home")
    public String showHomePage() {
        return "home"; // This should map to home.html
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
