package com.rh.rh_capsule.domain;


import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class CapsuleBox {

    @Id @GeneratedValue
    @Column(name = "capsule_box_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_email", nullable = false)
    private User user;

    private String theme;

    private LocalDateTime openedAt;
    private LocalDateTime closedAt;
    private LocalDateTime createdAt;


}
