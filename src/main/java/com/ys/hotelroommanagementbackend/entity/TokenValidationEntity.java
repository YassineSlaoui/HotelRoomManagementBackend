package com.ys.hotelroommanagementbackend.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tokens")
public class TokenValidationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(unique = true)
    private String token;

    private String tokenType;

    private boolean invalidated;

    @Column(name = "invalidated_at")
    private Date invalidatedAt;

    @Column(name = "expires_at")
    private Date expiresAt;
}