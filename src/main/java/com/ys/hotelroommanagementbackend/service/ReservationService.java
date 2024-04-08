package com.ys.hotelroommanagementbackend.service;

import com.ys.hotelroommanagementbackend.dto.ReservationDTO;
import com.ys.hotelroommanagementbackend.entity.Reservation;
import org.springframework.data.domain.Page;

import java.util.Date;

public interface ReservationService {

    Reservation findReservationById(Long reservationId);

    Page<ReservationDTO> findAllReservations(int page, int size);

    Page<ReservationDTO> findReservationsByGuestAndRoom(Long guestId, Long roomId, int page, int size);

    Page<ReservationDTO> findReservationsByGuest(Long guestId, int page, int size);

    Page<ReservationDTO> findReservationsEndingOn(Date endDate, int page, int size);

    Page<ReservationDTO> findReservationsStartingOn(Date startDate, int page, int size);

    Page<ReservationDTO> findReservationsStartingFrom(Date startDate, int page, int size);

    ReservationDTO findReservationByGuestAndRoomAndCheckInDate(Long guestId, Long roomId, Date checkInDate);

    ReservationDTO createReservation(ReservationDTO reservationDTO);

    ReservationDTO updateReservation(ReservationDTO reservationDTO);

    void deleteReservation(Long reservationId);
}
