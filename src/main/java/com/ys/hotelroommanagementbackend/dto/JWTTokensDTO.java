package com.ys.hotelroommanagementbackend.dto;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JWTTokensDTO {

    private String accessToken;

    private String refreshToken;

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", accessToken);
        map.put("refreshToken", refreshToken);
        return map;
    }
}
