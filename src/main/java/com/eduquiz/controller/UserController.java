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

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping
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
            return "redirect:login";
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
                model.addAttribute("username", user.getUsername());
                return "home";
            }
        }

        System.out.println("Login failed, invalid credentials");
        model.addAttribute("error", "Invalid username or password");
        return "login";
    }

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        String username = principal.getName(); // Get the username of the logged-in user

        // Fetch the user from the database using the username
        User user = userService.findByUsername(username);

        // Add the username to the model
        model.addAttribute("username", user.getUsername());

        return "home"; // Return the home.html view
    }


    private void addUsernameToModel(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        model.addAttribute("username", user.getUsername());
    }

    @GetMapping("/results")
    public String getResultsPage(Model model, Principal principal) {
        // Add username to the model
        addUsernameToModel(model, principal);

        // Existing logic
        List<Result> results = resultService.getAllResults();
        model.addAttribute("results", results);
        return "results"; // This will resolve to "results.html"
    }

    @GetMapping("/schools")
    public String viewSchools(Model model, Principal principal) {
        // Add username to the model
        addUsernameToModel(model, principal);

        // Existing logic
        List<School> schools = schoolService.getAllSchools();
        model.addAttribute("schools", schools);
        return "schools"; // Ensure school.html exists in src/main/resources/templates
    }

    @GetMapping("/quiz")
    public String quizPage(Model model, Principal principal) {
        // Add username to the model
        addUsernameToModel(model, principal);

        // Existing logic
        return "quiz"; // Thymeleaf template name
    }


    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
