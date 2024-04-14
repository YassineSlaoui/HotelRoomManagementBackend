package com.ys.hotelroommanagementbackend.helper;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ys.hotelroommanagementbackend.constant.JWTUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JWTHelper {

    final Algorithm algorithm = Algorithm.HMAC256(JWTUtil.SECRET);

    public String generateAccessToken(String usernameOrEmail, List<String> roles) {
        return JWT.create()
                .withSubject(usernameOrEmail)
                .withExpiresAt(DateUtils.addHours(new Date(), JWTUtil.EXPIRE_ACCESS_TOKEN))
                .withIssuer(JWTUtil.ISSUER)
                .withClaim("roles", roles)
                .sign(algorithm);
    }

    public String generateRefreshToken(String usernameOrEmail) {
        return JWT.create()
                .withSubject(usernameOrEmail)
                .withExpiresAt(DateUtils.addHours(new Date(), JWTUtil.EXPIRE_REFRESH_TOKEN))
                .withIssuer(JWTUtil.ISSUER)
                .sign(algorithm);
    }

    public String extractTokenFromHeaderIfExists(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(JWTUtil.BEARER_PREFIX)) {
            return authorizationHeader.substring(JWTUtil.BEARER_PREFIX.length());
        }
        return null;
    }

    public Map<String, String> getTokensMap(String jwtAccessToken, String jwtRefreshToken) {
        Map<String, String> idTokens = new HashMap<>();
        idTokens.put("accessToken", jwtAccessToken);
        idTokens.put("refreshToken", jwtRefreshToken);
        return idTokens;
    }
}
