package com.ys.hotelroommanagementbackend.service;

import com.ys.hotelroommanagementbackend.entity.User;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

public interface TokenValidationService {

    void insertToken(String token, Date ExpiresAt, User user, String tokenType);

    boolean isTokenValid(String token);

    void invalidateUserTokens(User user);

    /**
     * Remove all expired tokens from the database at 1AM every day
     *
     * @author Yassine Slaoui
     */
    @Scheduled(cron = "0 0 1 * * *")
    void removeExpiredTokens();
}
