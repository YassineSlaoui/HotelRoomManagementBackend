package com.ys.hotelroommanagementbackend.security;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
@EnableScheduling
public class InvalidatedTokenServiceImpl implements InvalidatedTokenService {

    InvalidatedTokenDao invalidatedTokenDao;

    public InvalidatedTokenServiceImpl(InvalidatedTokenDao invalidatedTokenDao) {
        this.invalidatedTokenDao = invalidatedTokenDao;
    }

    @Override
    public void invalidateToken(String token, Date ExpiresAt) {
        invalidatedTokenDao.save(InvalidatedTokenEntity.builder()
                .token(token)
                .invalidatedAt(new Date())
                .expiresAt(ExpiresAt)
                .build());
    }

    @Override
    public boolean isTokenInvalid(String token) {
        return invalidatedTokenDao.findByToken(token).isPresent();
    }

    /**
     * Remove all expired tokens from the database at 1AM every day
     *
     * @author: Yassine Slaoui
     */
    @Override
    @Scheduled(cron = "0 0 1 * * *")
    public void removeExpiredTokens() {
        invalidatedTokenDao.deleteAllByExpiresAtBefore(new Date());
    }
}
