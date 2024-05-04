package com.rh.rh_capsule.capsule.repository;

import com.rh.rh_capsule.capsule.domain.Capsule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CapsuleRepository extends JpaRepository<Capsule, Long> {
    List<Capsule> findByCapsuleBoxId(Long capsuleBoxId);
}
