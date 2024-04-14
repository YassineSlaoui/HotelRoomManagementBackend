package com.ys.hotelroommanagementbackend.security;

import com.ys.hotelroommanagementbackend.filter.JWTAuthenticationFilter;
import com.ys.hotelroommanagementbackend.filter.JWTAuthorizationFilter;
import com.ys.hotelroommanagementbackend.helper.JWTHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JWTHelper jwtHelper;

    private final TokenValidationService invalidatedTokenService;

    public SecurityConfiguration(JWTHelper jwtHelper, TokenValidationService invalidatedTokenService) {
        this.jwtHelper = jwtHelper;
        this.invalidatedTokenService = invalidatedTokenService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/v1/noauth/**").permitAll()
                        .requestMatchers("/api/v1/users/refresh-token").permitAll()
                        .requestMatchers("/v2/**",
                                "/v3/**",
                                "/api-docs/**",
                                "/swagger-resources/**",
                                "/swagger-ui/**").permitAll()
                        .anyRequest().authenticated())
                .addFilter(new JWTAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)), jwtHelper))
                .addFilterBefore(new JWTAuthorizationFilter(jwtHelper, invalidatedTokenService), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

}
