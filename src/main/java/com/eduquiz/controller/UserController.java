package com.eduquiz.controller;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.eduquiz.model.Result;
import com.eduquiz.model.School;
import com.eduquiz.model.User;
import com.eduquiz.service.ResultService;
import com.eduquiz.service.SchoolService;
import com.eduquiz.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
    private PasswordEncoder passwordEncoder;

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
                            Model model, HttpServletRequest request) {
        System.out.println("Attempting login for username: " + username);
        User user = userService.findByUsername(username);
        if (user != null) {
            boolean passwordMatch = passwordEncoder.matches(password, user.getPassword());
            if (passwordMatch) {
                System.out.println("Login successful, redirecting to /home");
                request.getSession().setAttribute("username", user.getUsername());
                return "redirect:/home"; // Redirect to home after successful login
            }
        }
        System.out.println("Login failed, invalid credentials");
        model.addAttribute("error", "Invalid username or password");
        return "login";
    }

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("username", user.getUsername());
        } else {
            model.addAttribute("username", "Guest"); // Handle unauthenticated user
        }
        return "home";
    }

    private void addUsernameToModel(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("username", user.getUsername());
        } else {
            model.addAttribute("username", "Guest"); // Handle unauthenticated user
        }
    }

    @GetMapping("/results")
    public String getResultsPage(Model model, Principal principal) {
        addUsernameToModel(model, principal);
        List<Result> results = resultService.getAllResults();
        model.addAttribute("results", results);
        return "results";
    }

    @GetMapping("/schools")
    public String viewSchools(Model model, Principal principal) {
        addUsernameToModel(model, principal);
        List<School> schools = schoolService.getAllSchools();
        model.addAttribute("schools", schools);
        return "schools";
    }

    @GetMapping("/quiz")
    public String quizPage(Model model, Principal principal) {
        addUsernameToModel(model, principal);
        return "quiz";
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        return "redirect:/login?logout";
    }
}