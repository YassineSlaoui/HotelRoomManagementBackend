package com.ys.hotelroommanagementbackend.service.impl;

import com.ys.hotelroommanagementbackend.dto.JWTTokensDTO;
import com.ys.hotelroommanagementbackend.helper.JWTHelper;
import com.ys.hotelroommanagementbackend.security.UserDetailsServiceImpl;
import com.ys.hotelroommanagementbackend.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JWTHelper jwtHelper;

    public AuthServiceImpl(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService, JWTHelper jwtHelper) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtHelper = jwtHelper;
    }


    @Override
    public JWTTokensDTO authenticate(String usernameOrEmail, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(usernameOrEmail, password));

        var user = userDetailsService.loadUserByUsername(usernameOrEmail);
        String jwtAccessToken = jwtHelper.generateAccessToken(user.getUsername(), user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
        String jwtRefreshToken = jwtHelper.generateRefreshToken(user.getUsername());

        return new JWTTokensDTO(jwtAccessToken, jwtRefreshToken);
    }
}
