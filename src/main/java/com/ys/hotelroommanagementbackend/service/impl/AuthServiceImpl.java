package com.ys.hotelroommanagementbackend.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ys.hotelroommanagementbackend.dto.JWTTokensDTO;
import com.ys.hotelroommanagementbackend.entity.Role;
import com.ys.hotelroommanagementbackend.entity.User;
import com.ys.hotelroommanagementbackend.exception.InvalidInputException;
import com.ys.hotelroommanagementbackend.helper.JWTHelper;
import com.ys.hotelroommanagementbackend.service.TokenValidationService;
import com.ys.hotelroommanagementbackend.security.UserDetailsServiceImpl;
import com.ys.hotelroommanagementbackend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static com.ys.hotelroommanagementbackend.constant.JWTUtil.AUTH_HEADER;
import static com.ys.hotelroommanagementbackend.constant.JWTUtil.SECRET;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JWTHelper jwtHelper;
    private final TokenValidationService invalidatedTokenService;
    private final UserServiceImpl userService;

    public AuthServiceImpl(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService, JWTHelper jwtHelper, TokenValidationService invalidatedTokenService, UserServiceImpl userService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtHelper = jwtHelper;
        this.invalidatedTokenService = invalidatedTokenService;
        this.userService = userService;
    }


    @Override
    public JWTTokensDTO authenticate(String usernameOrEmail, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(usernameOrEmail, password));

        var user = userDetailsService.loadUserByUsername(usernameOrEmail);
        String jwtAccessToken = jwtHelper.generateAccessToken(user.getUsername(), user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
        String jwtRefreshToken = jwtHelper.generateRefreshToken(user.getUsername());

        return new JWTTokensDTO(jwtAccessToken, jwtRefreshToken);
    }

    @Override
    public JWTTokensDTO handleRefreshToken(HttpServletRequest request) {
        String jwtRefreshToken = jwtHelper.extractTokenFromHeaderIfExists(request.getHeader(AUTH_HEADER));
        if (!invalidatedTokenService.isTokenValid(jwtRefreshToken))
            throw new InvalidInputException("Refresh token is invalid");
        if (jwtRefreshToken != null) {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(jwtRefreshToken);
            String usernameOrEmail = decodedJWT.getSubject();
            User user = userService.getUserByUsernameOrEmail(usernameOrEmail);
            invalidatedTokenService.invalidateUserTokens(user);
            String jwtAccessToken = jwtHelper.generateAccessToken(usernameOrEmail, user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
            String newJwtRefreshToken = jwtHelper.generateRefreshToken(user.getUsername());
            return JWTTokensDTO.builder().accessToken(jwtAccessToken).refreshToken(newJwtRefreshToken).build();
        } else
            throw new InvalidInputException("Refresh token required");
    }
}
