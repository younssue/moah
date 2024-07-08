package org.dessert.moah.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "session")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long id;

    // TODO: email로 변경필요
    private String email;
    @Column(name = "refresh_token")
    private String refreshToken;
    // 만료 시간
    private String expiration;
}
