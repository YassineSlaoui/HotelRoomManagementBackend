package com.ys.hotelroommanagementbackend.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ReservationDTO {

    private Long reservationId;

    private RoomDTO room;

    private GuestDTO guest;

    private ReviewDTO review;

    private Date checkInDate;

    private Date checkOutDate;

    private Boolean isActive;
}
