package com.ys.hotelroommanagementbackend.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestDTO {

    private long guestId;

    private String firstName;

    private String lastName;

    private String contactInfo;

    private UserDTO user;
}
