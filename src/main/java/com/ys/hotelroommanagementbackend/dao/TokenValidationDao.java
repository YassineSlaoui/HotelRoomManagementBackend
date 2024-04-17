package com.ys.hotelroommanagementbackend.dao;

import com.ys.hotelroommanagementbackend.entity.User;
import com.ys.hotelroommanagementbackend.entity.TokenValidationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TokenValidationDao extends JpaRepository<TokenValidationEntity, Long> {

    Optional<TokenValidationEntity> findByToken(String token);

    void deleteAllByExpiresAtBefore(Date date);

    List<TokenValidationEntity> findTokensByUser(User user);
}
