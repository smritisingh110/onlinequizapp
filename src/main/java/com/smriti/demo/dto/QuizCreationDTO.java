
// Create this DTO class
package com.smriti.demo.dto;

import java.util.List;
import java.util.Map;

public class QuizCreationDTO {
    private String title;
    private Map<String, QuestionDTO> questions;
    
    public static class QuestionDTO {
        private String text;
        private Map<String, String> options;
        
        // Getters and setters
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
        public Map<String, String> getOptions() { return options; }
        public void setOptions(Map<String, String> options) { this.options = options; }
    }
    
    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Map<String, QuestionDTO> getQuestions() { return questions; }
    public void setQuestions(Map<String, QuestionDTO> questions) { this.questions = questions; }
}