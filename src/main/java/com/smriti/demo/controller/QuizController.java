package com.smriti.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smriti.demo.model.Quiz;
import com.smriti.demo.model.Question;
import com.smriti.demo.model.Option;
import com.smriti.demo.repository.QuizRepository;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class QuizController {
    private final QuizRepository quizRepository;

    public QuizController(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    // Show all quizzes
    @GetMapping("/list-quiz")
    public String listQuizzes(Model model) {
        List<Quiz> quizzes = quizRepository.findAll();
        model.addAttribute("quizzes", quizzes);
        return "list-quiz";
    }

    // Handle quiz creation with questions and options
    @PostMapping("/create")
    public String createQuiz(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            String title = request.getParameter("title");
            if (title == null || title.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Quiz title is required!");
                return "redirect:/create";
            }

            Quiz quiz = new Quiz();
            quiz.setTitle(title.trim());

            // Process questions
            Map<String, String[]> paramMap = request.getParameterMap();
            Map<String, Question> questionsMap = new HashMap<>();

            // Find all question parameters
            for (String paramName : paramMap.keySet()) {
                if (paramName.startsWith("questions[") && paramName.endsWith("].text")) {
                    // Extract question number: questions[1].text -> 1
                    String questionNum = paramName.substring(10, paramName.indexOf("].text"));
                    String questionText = request.getParameter(paramName);
                    
                    if (questionText != null && !questionText.trim().isEmpty()) {
                        Question question = new Question();
                        question.setText(questionText.trim());
                        question.setQuiz(quiz);
                        questionsMap.put(questionNum, question);
                    }
                }
            }

            // Process options for each question
            for (String paramName : paramMap.keySet()) {
                if (paramName.startsWith("questions[") && paramName.contains("].options[")) {
                    // Extract question number: questions[1].options[2] -> 1
                    String questionNum = paramName.substring(10, paramName.indexOf("].options["));
                    String optionText = request.getParameter(paramName);
                    
                    if (questionsMap.containsKey(questionNum) && optionText != null && !optionText.trim().isEmpty()) {
                        Question question = questionsMap.get(questionNum);
                        Option option = new Option();
                        option.setText(optionText.trim());
                        option.setQuestion(question);
                        question.getOptions().add(option);
                    }
                }
            }

            // Add questions to quiz
            quiz.getQuestions().addAll(questionsMap.values());

            if (quiz.getQuestions().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "At least one question is required!");
                return "redirect:/create";
            }

            quizRepository.save(quiz);
            redirectAttributes.addFlashAttribute("success", "Quiz created successfully!");
            return "redirect:/list-quiz";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating quiz: " + e.getMessage());
            return "redirect:/create";
        }
    }

    // Display quiz for taking
    @GetMapping("/quiz/{id}")
    public String takeQuiz(@PathVariable Long id, Model model) {
        Optional<Quiz> optionalQuiz = quizRepository.findById(id);
        if (optionalQuiz.isPresent()) {
            model.addAttribute("quiz", optionalQuiz.get());
            return "take-quiz-page";
        } else {
            return "redirect:/list-quiz";
        }
    }

    // Delete quiz
    @PostMapping("/delete/{id}")
    public String deleteQuiz(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            quizRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Quiz deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting quiz: " + e.getMessage());
        }
        return "redirect:/list-quiz";
    }
}