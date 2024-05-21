package com.rh.rh_capsule.inquiry.domain;

import com.rh.rh_capsule.auth.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Inquiry {
    @Id @GeneratedValue
    @Column(name = "inquiry_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean isAnswered;
    @Builder
    public Inquiry(String content, User user, LocalDateTime createdAt) {
        this.content = content;
        this.user = user;
        this.createdAt = createdAt;
        this.isAnswered = false;
    }
}

