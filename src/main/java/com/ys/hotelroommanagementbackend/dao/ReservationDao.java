package com.ys.hotelroommanagementbackend.dao;

import com.ys.hotelroommanagementbackend.entity.Guest;
import com.ys.hotelroommanagementbackend.entity.Reservation;
import com.ys.hotelroommanagementbackend.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface ReservationDao extends JpaRepository<Reservation, Long> {

    Page<Reservation> findReservationsByGuest(Guest guest, Pageable pageable);

    Page<Reservation> findReservationsByGuestAndRoom(Guest guest, Room room, Pageable pageable);

    Page<Reservation> findReservationsByRoom(Room room, Pageable pageable);

    Page<Reservation> findReservationsByCheckOutDate(Date checkOutDate, Pageable pageable);

    Page<Reservation> findReservationsByCheckInDate(Date checkInDate, Pageable pageable);

    Page<Reservation> findReservationsByCheckInDateAfter(Date startCheckInDate, Pageable pageable);

    Optional<Reservation> findReservationByGuestAndRoomAndCheckInDate(Guest guest, Room room, Date checkInDate);

    Optional<Reservation> findReservationByRoomAndCheckInDateBeforeAndCheckOutDateAfter(Room room, Date checkInDate, Date checkOutDate);

    List<Reservation> findReservationsByCheckInDateAfterOrCheckOutDateAfter(Date startCheckInDate, Date endCheckInDate);

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.room = :room " +
            "AND r.checkOutDate > :checkInDate " +
            "AND r.checkInDate < :checkOutDate")
    List<Reservation> findOverlappingReservationsForRoom(@Param("room") Room room,
                                                         @Param("checkInDate") Date checkInDate,
                                                         @Param("checkOutDate") Date checkOutDate);

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.checkOutDate > :checkInDate " +
            "AND r.checkInDate < :checkOutDate")
    List<Reservation> findReservationsForDateRange(@Param("checkInDate") Date checkInDate,
                                                   @Param("checkOutDate") Date checkOutDate);
}
