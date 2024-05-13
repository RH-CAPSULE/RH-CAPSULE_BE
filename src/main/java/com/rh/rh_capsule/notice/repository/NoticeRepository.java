package com.rh.rh_capsule.notice.repository;

import com.rh.rh_capsule.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

}
