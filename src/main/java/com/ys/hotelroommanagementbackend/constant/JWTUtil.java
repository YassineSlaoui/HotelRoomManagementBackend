package com.ys.hotelroommanagementbackend.constant;

public class JWTUtil {
    public static final Integer EXPIRE_ACCESS_TOKEN = 1;
    public static final Integer EXPIRE_REFRESH_TOKEN = 2;
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String ISSUER = "springBootApp";
    public static final String SECRET = "myPrivateSecret";
    public static final String AUTH_HEADER = "Authorization";
}
