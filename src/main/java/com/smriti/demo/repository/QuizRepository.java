package com.smriti.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smriti.demo.model.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
