package com.rh.rh_capsule.auth.repository;

import com.rh.rh_capsule.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByUserEmail(String userEmail);
    Boolean existsByPassword(String password);

    User findByUserEmail(String userEmail);
    Optional<User> findById(Long id);
}
