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
                                        "/js/script.js",
                                        "/css/styles.css",
                                        "/register",
                                        "/login",
                                        "/submit_register",
                                        "/submit_login",
                                        "/schools",
                                        "/results",
                                        "/home"
                                ).permitAll()
                                // Protect specific endpoints that need authentication
                                .requestMatchers(
                                        "/profile",
                                        "/quiz"
                                ).authenticated()
                                // Any other request should be accessible without authentication
                                .anyRequest().permitAll()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")  // URL for login page
                                .loginProcessingUrl("/submit_login") // Custom URL for form submission
                                .defaultSuccessUrl("/home", true)  // Redirect after successful login
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutSuccessUrl("/login")  // Redirect after logout
                                .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
