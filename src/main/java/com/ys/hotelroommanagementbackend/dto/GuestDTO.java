package com.ys.hotelroommanagementbackend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class GuestDTO {

    private Long guestId;

    private String firstName;

    private String lastName;

    private String contactInfo;

    private UserDTO user;
}
