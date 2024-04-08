package com.ys.hotelroommanagementbackend.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDTO {

    @Setter(AccessLevel.NONE)
    private Long reservationId;

    private RoomDTO room;

    private GuestDTO guest;

    private ReviewDTO review;

    private Date checkInDate;

    private Date checkOutDate;

    private Boolean isActive;
}
