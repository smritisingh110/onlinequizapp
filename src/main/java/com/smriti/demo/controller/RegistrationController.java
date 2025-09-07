package com.smriti.demo.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import com.smriti.demo.model.User;
import com.smriti.demo.repository.UserRepository;

@Controller
public class RegistrationController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // @GetMapping("/register")
    // public String showRegistrationForm() {
    //     return "register";
    // }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            model.addAttribute("error", "Username already exists!");
            return "register";
        }
        
        if (userRepository.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "Email already exists!");
            return "register";
        }
        
        // Encrypt password and save user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        
        return "redirect:/login?registered=true";
    }
}