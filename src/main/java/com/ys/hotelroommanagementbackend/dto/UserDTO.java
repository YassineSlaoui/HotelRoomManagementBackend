package com.ys.hotelroommanagementbackend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private String username;

    private String email;

    private String password;
}
