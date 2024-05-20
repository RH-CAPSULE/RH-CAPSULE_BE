package com.rh.rh_capsule.capsule.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(CapsuleEntityListener.class)
public class Capsule {

    @Id @GeneratedValue
    @Column(name = "capsule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capsule_box_id", nullable = false)
    private CapsuleBox capsuleBox;

    @Column(nullable = false)
    private String color;
    @Column(nullable = false)
    private String theme;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private String writer;

    private String imageUrl;
    private String audioUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private Boolean isMine;

    @Builder
    public Capsule(CapsuleBox capsuleBox, String color, String theme, String title, String content, String writer, String imageUrl, String audioUrl, LocalDateTime createdAt, Boolean isMine) {
        this.capsuleBox = capsuleBox;
        this.color = color;
        this.theme = theme;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.imageUrl = imageUrl;
        this.audioUrl = audioUrl;
        this.createdAt = createdAt;
        this.isMine = isMine;
    }
    protected Capsule() {
    }
}
