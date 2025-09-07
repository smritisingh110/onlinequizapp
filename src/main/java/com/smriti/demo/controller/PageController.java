package com.smriti.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.smriti.demo.repository.QuizRepository;

@Controller
public class PageController {

    private final QuizRepository quizRepository;

    public PageController(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    @GetMapping("/")
    public String home() {
        // Check if user is authenticated
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/dashboard";
        }
        return "redirect:/login"; // Redirect to login if not authenticated
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // This is your main page after login
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        model.addAttribute("quizzes", quizRepository.findAll());
        return "dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @GetMapping("/create")
    public String createQuiz() {
        return "create-quiz";
    }
}