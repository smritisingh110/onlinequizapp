// UPDATED PageController to handle login page display
package com.smriti.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String index() {
        return "index"; 
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/create")
    public String createQuiz() {
        return "create-quiz";
    }

    // Add this to handle login page display
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}