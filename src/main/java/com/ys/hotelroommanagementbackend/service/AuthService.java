package com.ys.hotelroommanagementbackend.service;

import com.ys.hotelroommanagementbackend.dto.JWTTokensDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthService {
    JWTTokensDTO authenticate(String usernameOrEmail, String password);

    JWTTokensDTO handleRefreshToken(HttpServletRequest request);
}
