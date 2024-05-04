package com.rh.rh_capsule.capsule.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Capsule {

    @Id @GeneratedValue
    @Column(name = "capsule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capsule_box_id", nullable = false)
    private CapsuleBox capsuleBox;

    private String color;
    private String title;
    private String content;
    private String writer;

    private String imageUrl;
    private String audioUrl;

    private LocalDateTime createdAt;
    private Boolean isMine;

    @Builder
    public Capsule(CapsuleBox capsuleBox, String color, String title, String content, String writer, String imageUrl, String audioUrl, LocalDateTime createdAt, Boolean isMine) {
        this.capsuleBox = capsuleBox;
        this.color = color;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.imageUrl = imageUrl;
        this.audioUrl = audioUrl;
        this.createdAt = createdAt;
        this.isMine = isMine;
    }
}
