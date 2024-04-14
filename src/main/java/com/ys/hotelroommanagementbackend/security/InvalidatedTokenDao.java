package com.ys.hotelroommanagementbackend.security;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface InvalidatedTokenDao extends JpaRepository<InvalidatedTokenEntity, Long> {

    Optional<InvalidatedTokenEntity> findByToken(String token);

    void deleteByToken(String token);

    void deleteAllByExpiresAtBefore(Date date);
}
