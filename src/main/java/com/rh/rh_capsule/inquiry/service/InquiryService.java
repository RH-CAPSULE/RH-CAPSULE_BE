package com.rh.rh_capsule.inquiry.service;

import com.rh.rh_capsule.auth.domain.User;
import com.rh.rh_capsule.auth.exception.AuthErrorCode;
import com.rh.rh_capsule.auth.exception.AuthException;
import com.rh.rh_capsule.auth.repository.UserRepository;
import com.rh.rh_capsule.inquiry.controller.dto.InquiryDTO;
import com.rh.rh_capsule.inquiry.domain.Inquiry;
import com.rh.rh_capsule.inquiry.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InquiryService {
    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;
    public void createInquiry(InquiryDTO inquiryDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND));

        Inquiry inquiry = Inquiry.builder()
                .content(inquiryDTO.content())
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        inquiryRepository.save(inquiry);
    }
}
