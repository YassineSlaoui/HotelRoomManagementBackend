package com.ys.hotelroommanagementbackend.dao;

import com.ys.hotelroommanagementbackend.entity.Guest;
import com.ys.hotelroommanagementbackend.entity.Reservation;
import com.ys.hotelroommanagementbackend.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface ReservationDao extends JpaRepository<Reservation, Long> {

    Page<Reservation> findReservationsByGuest(Guest guest, Pageable pageable);

    Page<Reservation> findReservationsByGuestAndRoom(Guest guest, Room room, Pageable pageable);

    Page<Reservation> findReservationsByRoom(Room room, Pageable pageable);

    Page<Reservation> findReservationsByCheckOutDate(Date checkOutDate, Pageable pageable);

    Page<Reservation> findReservationsByCheckInDate(Date checkInDate, Pageable pageable);

    Page<Reservation> findReservationsByCheckInDateAfter(Date startCheckInDate, Pageable pageable);

    Optional<Reservation> findReservationByGuestAndRoomAndCheckInDate(Guest guest, Room room, Date checkInDate);
}
