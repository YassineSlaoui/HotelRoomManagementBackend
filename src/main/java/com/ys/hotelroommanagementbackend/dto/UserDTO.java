package com.ys.hotelroommanagementbackend.dto;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDTO {

    private Long userId;

    private String username;

    private String email;

    @ToString.Exclude
    private String password;

    @Builder.Default
    private Set<String> roles = new HashSet<>();
}
