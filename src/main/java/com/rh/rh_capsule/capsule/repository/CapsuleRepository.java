package com.rh.rh_capsule.capsule.repository;

import com.rh.rh_capsule.capsule.domain.Capsule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CapsuleRepository extends JpaRepository<Capsule, Long> {
    Page<Capsule> findByCapsuleBoxId(Long capsuleBoxId, Pageable pageable);
}
