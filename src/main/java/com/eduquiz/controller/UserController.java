package com.eduquiz.controller;

import com.eduquiz.model.Result;
import com.eduquiz.model.School;
import com.eduquiz.model.User;
import com.eduquiz.service.ResultService;
import com.eduquiz.service.SchoolService;
import com.eduquiz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    private ResultService resultService;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("quizUser", new User());
        return "register";
    }

    @PostMapping("/submit_register")
    public String registerUser(@ModelAttribute("quizUser") User user, Model model) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String result = userService.registerUser(user);
        if ("User registered successfully".equals(result)) {
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
    public String home(Model model, @AuthenticationPrincipal User user) {
        if (user != null) {
            System.out.println("User authenticated: " + user.getUsername());
            model.addAttribute("username", user.getUsername());
        } else {
            System.out.println("User not authenticated");
            model.addAttribute("username", "Guest");
        }
        return "home";
    }



    @GetMapping("/results")
    public String getResultsPage(Model model) {
        List<Result> results = resultService.getAllResults();
        model.addAttribute("results", results);
        return "results"; // This will resolve to "results.html"
    }

    @GetMapping("/view")
    public String viewSchools(Model model) {
        List<School> schools = schoolService.getAllSchools();
        model.addAttribute("schools", schools);
        return "schools"; // Ensure school.html exists in src/main/resources/templates
    }

    @GetMapping("/quiz")
    public String quizPage() {
        return "quiz"; // Thymeleaf template name
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
