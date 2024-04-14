package com.ys.hotelroommanagementbackend.security;

import com.ys.hotelroommanagementbackend.entity.User;
import com.ys.hotelroommanagementbackend.service.UserService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
@EnableScheduling
public class TokenValidationServiceImpl implements TokenValidationService {

    private final TokenValidationDao tokenValidationDao;

    public TokenValidationServiceImpl(TokenValidationDao tokenValidationDao) {
        this.tokenValidationDao = tokenValidationDao;
    }

    @Override
    public void insertToken(String token, Date ExpiresAt, User user, String tokenType) {
        tokenValidationDao.save(TokenValidationEntity.builder()
                .token(token)
                .expiresAt(ExpiresAt)
                .user(user)
                .tokenType(tokenType)
                .invalidated(false)
                .build());
    }

    @Override
    public boolean isTokenValid(String token) {
        return !tokenValidationDao.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token not found"))
                .isInvalidated();
    }

    @Override
    public void invalidateUserTokens(User user) {
        tokenValidationDao.findTokensByUser(user).forEach(tokenValidationEntity -> {
            tokenValidationEntity.setInvalidated(true);
            tokenValidationEntity.setInvalidatedAt(new Date());
            tokenValidationDao.save(tokenValidationEntity);
        });
    }

    /**
     * Remove all expired tokens from the database at 1AM every day
     *
     * @author Yassine Slaoui
     */
    @Override
    @Scheduled(cron = "0 0 1 * * *")
    public void removeExpiredTokens() {
        tokenValidationDao.deleteAllByExpiresAtBefore(new Date());
    }
}
