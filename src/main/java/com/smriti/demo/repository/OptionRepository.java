package com.smriti.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.smriti.demo.model.Option;

public interface OptionRepository extends JpaRepository<Option, Long> {
}