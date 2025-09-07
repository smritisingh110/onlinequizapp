package com.smriti.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.smriti.demo.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
