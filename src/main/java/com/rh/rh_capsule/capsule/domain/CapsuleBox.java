package com.rh.rh_capsule.capsule.domain;


import com.rh.rh_capsule.auth.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class CapsuleBox {

    @Id @GeneratedValue
    @Column(name = "capsule_box_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "box_user", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private CapsuleBoxTheme theme;

    private LocalDateTime openedAt;
    private LocalDateTime closedAt;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "capsuleBox", fetch = FetchType.LAZY)
    private List<Capsule> capsules = new ArrayList<>();

    @Builder
    public CapsuleBox(User user, CapsuleBoxTheme theme, LocalDateTime openedAt, LocalDateTime closedAt, LocalDateTime createdAt) {
        this.user = user;
        this.theme = theme;
        this.openedAt = openedAt;
        this.closedAt = closedAt;
        this.createdAt = createdAt;
    }
    protected CapsuleBox() {
    }
}
