package com.ys.hotelroommanagementbackend.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.hotelroommanagementbackend.helper.JWTHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JWTHelper jwtHelper;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTHelper jwtHelper) {
        this.authenticationManager = authenticationManager;
        this.jwtHelper = jwtHelper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String usernameOrEmail = request.getParameter("username");
        if (usernameOrEmail == null)
            usernameOrEmail = request.getParameter("email");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usernameOrEmail, password);

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        User user = (User) authResult.getPrincipal();
        String jwtAccessToken = jwtHelper.generateAccessToken(user.getUsername(), user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
        String jwtRefreshToken = jwtHelper.generateRefreshToken(user.getUsername());
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getWriter(), jwtHelper.getTokensMap(jwtAccessToken, jwtRefreshToken));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getWriter(), failed.getMessage());
    }
}
