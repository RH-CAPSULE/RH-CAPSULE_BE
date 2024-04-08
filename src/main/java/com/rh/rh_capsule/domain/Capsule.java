package com.rh.rh_capsule.domain;

import jakarta.persistence.*;
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
    private LocalDateTime createdAt;
    private Boolean isMine;

}
