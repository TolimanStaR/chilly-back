package com.chilly.security_svc.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity(name = "refresh_token")
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @SequenceGenerator(name = "refresh_token_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refresh_token_sequence")
    @Column(name = "id")
    private Long id;

    @Column(name = "token")
    private String token;

    @Column(name = "expiration")
    private Date expiration;

    @OneToOne(mappedBy = "refreshToken")
    private User user;

}
