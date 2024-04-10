package com.ys.hotelroommanagementbackend.service;

import com.ys.hotelroommanagementbackend.dto.ReservationDTO;
import com.ys.hotelroommanagementbackend.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

public interface ReservationService {

    Reservation getReservationById(Long reservationId);

    Page<ReservationDTO> getAllReservations(int page, int size);

    Page<ReservationDTO> getReservationsByGuestAndRoom(Long guestId, Long roomId, int page, int size);

    Page<ReservationDTO> getReservationsByGuest(Long guestId, int page, int size);

    Page<ReservationDTO> getReservationsByRoom(Long roomId, int page, int size);

    Page<ReservationDTO> getReservationsEndingOn(Date endDate, int page, int size);

    Page<ReservationDTO> getReservationsStartingOn(Date startDate, int page, int size);

    Page<ReservationDTO> getReservationsStartingFrom(Date startDate, int page, int size);

    ReservationDTO getReservationByGuestAndRoomAndCheckInDate(Long guestId, Long roomId, Date checkInDate);

    ReservationDTO createReservation(ReservationDTO reservationDTO);

    ReservationDTO updateReservation(ReservationDTO reservationDTO);

    void deleteReservation(Long reservationId);

    void addReviewToReservation(Long reservationId, Long reviewId);

    boolean isRoomAvailable(Long roomId, Date checkInDate, Date checkOutDate);

    @Scheduled(cron = "0 0 0 * * ?")
    void updateRoomsAvailability();
}
