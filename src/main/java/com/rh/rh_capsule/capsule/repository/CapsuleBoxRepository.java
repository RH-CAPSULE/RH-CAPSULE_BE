package com.rh.rh_capsule.capsule.repository;

import com.rh.rh_capsule.capsule.domain.CapsuleBox;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CapsuleBoxRepository extends JpaRepository<CapsuleBox, Long> {
    List<CapsuleBox> findByUserId(Long userId);
    Optional<CapsuleBox> findById(Long id);

    //페이지 네이션
    Page<CapsuleBox> findByUserIdAndOpenedAtAfter(Long userId, LocalDateTime openedAt, Pageable pageable);

}
