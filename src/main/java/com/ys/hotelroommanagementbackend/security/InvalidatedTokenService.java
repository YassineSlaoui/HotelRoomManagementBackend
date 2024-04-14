package com.ys.hotelroommanagementbackend.security;

import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

public interface InvalidatedTokenService {

    void invalidateToken(String token, Date ExpiresAt);

    boolean isTokenValid(String token);

    @Scheduled(cron = "0 0 1 * * *")
    void removeExpiredTokens();
}
