package com.rh.rh_capsule.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name="users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userEmail;

    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserAuthority authority;
}
