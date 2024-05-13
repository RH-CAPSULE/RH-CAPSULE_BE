package com.rh.rh_capsule.notice.service;

import com.rh.rh_capsule.notice.controller.dto.NoticeDTO;
import com.rh.rh_capsule.notice.domain.Notice;
import com.rh.rh_capsule.notice.exception.NoticeErrorCode;
import com.rh.rh_capsule.notice.exception.NoticeException;
import com.rh.rh_capsule.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    public NoticeDTO getLatestNotice() {
        List<Notice> notices = noticeRepository.findAll();
        if (notices.isEmpty()) {
            throw new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND);
        }

        Notice latestNotice = notices.stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .findFirst()
                .orElse(null);

        return new NoticeDTO(
                latestNotice.getId(),
                latestNotice.getTitle(),
                latestNotice.getContent(),
                latestNotice.getCreatedAt()
        );
    }
}
