package com.ys.hotelroommanagementbackend.mapper;

import com.ys.hotelroommanagementbackend.dto.ReservationDTO;
import com.ys.hotelroommanagementbackend.entity.Reservation;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class ReservationMapper {

    private final RoomMapper roomMapper;

    private final GuestMapper guestMapper;

    private final ReviewMapper reviewMapper;

    public ReservationMapper(RoomMapper roomMapper, GuestMapper guestMapper, ReviewMapper reviewMapper) {
        this.roomMapper = roomMapper;
        this.guestMapper = guestMapper;
        this.reviewMapper = reviewMapper;
    }

    public ReservationDTO fromReservation(Reservation reservation) {
        ReservationDTO reservationDTO = new ReservationDTO();
        BeanUtils.copyProperties(reservation, reservationDTO);
        reservationDTO.setRoom(roomMapper.fromRoom(reservation.getRoom()));
        reservationDTO.setGuest(guestMapper.fromGuest(reservation.getGuest()));
        if (reservation.getReview() != null)
            reservationDTO.setReview(reviewMapper.fromReview(reservation.getReview()));
        else
            reservationDTO.setReview(null);
        return reservationDTO;
    }

    public Reservation toReservation(ReservationDTO reservationDTO) {
        Reservation reservation = new Reservation();
        BeanUtils.copyProperties(reservationDTO, reservation);
        return reservation;
    }
}
