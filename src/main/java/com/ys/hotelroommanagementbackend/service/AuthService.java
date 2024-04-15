package com.ys.hotelroommanagementbackend.service;

import com.ys.hotelroommanagementbackend.dto.JWTTokensDTO;

public interface AuthService {
    public JWTTokensDTO authenticate(String usernameOrEmail, String password);
}
