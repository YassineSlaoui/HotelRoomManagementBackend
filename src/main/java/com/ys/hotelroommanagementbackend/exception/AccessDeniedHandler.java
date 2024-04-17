package com.ys.hotelroommanagementbackend.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AccessDeniedHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse res, AuthenticationException authException) throws IOException {
        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(403);
        Map<String, Object> data = new HashMap<>();
        data.put("timestamp", new Date());
        data.put("status", 403);
        data.put("message", "Access denied");
        new ObjectMapper().writeValue(res.getOutputStream(), data);
    }
}
