package com.eduquiz;

import com.eduquiz.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Consider enabling CSRF protection in production
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                // Permit access to static resources
                                .requestMatchers(
                                        "/js/script.js",           // Allow access to all JS files
                                        "/css/styles.css",          // Allow access to all CSS files
                                        "/api/users/register",
                                        "/api/users/login",
                                        "/api/users/submit_register",
                                        "/api/users/submit_login",
                                        "/api/schools/view",
                                        "/api/users/results",
                                        "/api/users/home",
                                        "/api/users/quiz"
                                ).permitAll()
                                // Permit access to all HTML pages
                                .requestMatchers(
                                        "/**/quiz.html"
                                ).permitAll()
                                // Protect only specific endpoints that need authentication
                                .requestMatchers(
                                        "/api/users/profile",   // Example: protect user profile page
                                        "/api/quizzes/**"          // Example: protect quiz-related pages
                                ).authenticated()
                                // All other requests should be accessible without authentication
                                .anyRequest().permitAll()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/api/users/login")  // URL for login page
                                .defaultSuccessUrl("/api/users/home", true)  // Redirect after successful login
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutSuccessUrl("/api/users/login")  // Redirect after logout
                                .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}



