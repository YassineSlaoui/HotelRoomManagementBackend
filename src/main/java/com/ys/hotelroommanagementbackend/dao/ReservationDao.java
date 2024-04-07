package com.ys.hotelroommanagementbackend.dao;

import com.ys.hotelroommanagementbackend.entity.Guest;
import com.ys.hotelroommanagementbackend.entity.Reservation;
import com.ys.hotelroommanagementbackend.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ReservationDao extends JpaRepository<Reservation, Long> {
    List<Reservation> findReservationsByGuest(Guest guest);

    List<Reservation> findReservationsByGuestAndRoom(Guest guest, Room room);

    Optional<Reservation> findReservationByGuestAndRoomAndCheckInDate(Guest guest, Room room, Date checkInDate);
}
