package com.rh.rh_capsule.repository;

import com.rh.rh_capsule.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    Boolean existsByUserEmail(String userEmail);
    Boolean existsByPassword(String password);

    User findByUserEmail(String userEmail);
}
