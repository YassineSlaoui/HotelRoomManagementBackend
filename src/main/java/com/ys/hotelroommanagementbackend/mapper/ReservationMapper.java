package com.ys.hotelroommanagementbackend.mapper;

import com.ys.hotelroommanagementbackend.dto.ReservationDTO;
import com.ys.hotelroommanagementbackend.entity.Reservation;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class ReservationMapper {

    private RoomMapper roomMapper;

    private GuestMapper guestMapper;

    private ReviewMapper reviewMapper;

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
        reservationDTO.setReview(reviewMapper.fromReview(reservation.getReview()));
        return reservationDTO;
    }

    public Reservation toReservation(ReservationDTO reservationDTO) {
        Reservation reservation = new Reservation();
        BeanUtils.copyProperties(reservationDTO, reservation);
        return reservation;
    }
}
