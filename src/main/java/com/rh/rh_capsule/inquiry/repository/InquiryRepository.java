package com.rh.rh_capsule.inquiry.repository;

import com.rh.rh_capsule.inquiry.domain.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

}
