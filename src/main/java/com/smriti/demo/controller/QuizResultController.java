package com.smriti.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.smriti.demo.model.Quiz;
import com.smriti.demo.model.Question;
import com.smriti.demo.repository.QuizRepository;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class QuizResultController {
    
    private final QuizRepository quizRepository;

    public QuizResultController(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    // Submit quiz and show results
    @PostMapping("/submit-quiz/{id}")
    public String submitQuiz(@PathVariable Long id, HttpServletRequest request, Model model) {
        try {
            Optional<Quiz> optionalQuiz = quizRepository.findById(id);
            if (!optionalQuiz.isPresent()) {
                return "redirect:/dashboard";
            }

            Quiz quiz = optionalQuiz.get();
            List<Question> questions = quiz.getQuestions();
            
            // Get current user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            // Calculate score and prepare results
            int totalQuestions = questions.size();
            int correctAnswers = 0;
            List<QuizResult> results = new ArrayList<>();

            for (Question question : questions) {
                String userAnswer = request.getParameter("question_" + question.getId());
                
                QuizResult result = new QuizResult();
                result.setQuestion(question.getText());
                result.setUserAnswer(userAnswer != null ? userAnswer : "No answer");
                result.setOptions(question.getOptions());
                
                // Find the correct answer from options
                String correctAnswer = "";
                for (com.smriti.demo.model.Option option : question.getOptions()) {
                    if (option.isCorrect()) {
                        correctAnswer = option.getText();
                        break;
                    }
                }
                result.setCorrectAnswer(correctAnswer);
                
                if (userAnswer != null && userAnswer.equals(correctAnswer)) {
                    correctAnswers++;
                    result.setCorrect(true);
                } else {
                    result.setCorrect(false);
                }
                
                results.add(result);
            }

            double percentage = totalQuestions > 0 ? (double) correctAnswers / totalQuestions * 100 : 0;

            model.addAttribute("quiz", quiz);
            model.addAttribute("username", username);
            model.addAttribute("totalQuestions", totalQuestions);
            model.addAttribute("correctAnswers", correctAnswers);
            model.addAttribute("percentage", Math.round(percentage * 100.0) / 100.0);
            model.addAttribute("results", results);

            return "quiz-results";

        } catch (Exception e) {
            return "redirect:/dashboard?error=submission_failed";
        }
    }

    // Inner class for quiz results
    public static class QuizResult {
        private String question;
        private String userAnswer;
        private String correctAnswer;
        private boolean correct;
        private List<com.smriti.demo.model.Option> options;

        // Getters and setters
        public String getQuestion() { return question; }
        public void setQuestion(String question) { this.question = question; }
        
        public String getUserAnswer() { return userAnswer; }
        public void setUserAnswer(String userAnswer) { this.userAnswer = userAnswer; }
        
        public String getCorrectAnswer() { return correctAnswer; }
        public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
        
        public boolean isCorrect() { return correct; }
        public void setCorrect(boolean correct) { this.correct = correct; }
        
        public List<com.smriti.demo.model.Option> getOptions() { return options; }
        public void setOptions(List<com.smriti.demo.model.Option> options) { this.options = options; }
    }
}