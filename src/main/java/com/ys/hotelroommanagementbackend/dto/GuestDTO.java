package com.ys.hotelroommanagementbackend.dto;

import lombok.AccessLevel;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestDTO {

    @Setter(AccessLevel.NONE)
    private long guestId;

    private String firstName;

    private String lastName;

    private String contactInfo;

    private Date createdDate;

    private Date lastModifiedDate;

    private UserDTO user;
}
