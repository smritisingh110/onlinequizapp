package com.smriti.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smriti.demo.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
