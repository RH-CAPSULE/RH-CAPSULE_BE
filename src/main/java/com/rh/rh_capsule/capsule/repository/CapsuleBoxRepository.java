package com.rh.rh_capsule.capsule.repository;

import com.rh.rh_capsule.auth.domain.User;
import com.rh.rh_capsule.capsule.domain.CapsuleBox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CapsuleBoxRepository extends JpaRepository<CapsuleBox, Long> {
    List<CapsuleBox> findByUser(User user);
    Optional<CapsuleBox> findById(Long id);
}
