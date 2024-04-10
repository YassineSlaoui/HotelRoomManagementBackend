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

public interface ReservationDao extends JpaRepository<Reservation, Long> {

    Page<Reservation> findReservationsByGuest(Guest guest, Pageable pageable);

    Page<Reservation> findReservationsByGuestAndRoom(Guest guest, Room room, Pageable pageable);

    Page<Reservation> findReservationsByRoom(Room room, Pageable pageable);

    Page<Reservation> findReservationsByCheckOutDate(Date checkOutDate, Pageable pageable);

    Page<Reservation> findReservationsByCheckInDate(Date checkInDate, Pageable pageable);

    Page<Reservation> findReservationsByCheckInDateAfter(Date startCheckInDate, Pageable pageable);

    Optional<Reservation> findReservationByGuestAndRoomAndCheckInDate(Guest guest, Room room, Date checkInDate);

    Optional<Reservation> findReservationByRoomAndCheckInDateBeforeAndCheckOutDateAfter(Room room, Date checkInDate, Date checkOutDate);

    @Query("SELECT COUNT(res) > 0 FROM Reservation res " +
            "WHERE res.room = :room AND " +
            "(:checkInDate < res.checkOutDate AND :checkOutDate > res.checkInDate)")
    boolean isRoomReserved(@Param("room") Room room,
                           @Param("checkInDate") Date checkInDate,
                           @Param("checkOutDate") Date checkOutDate);

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.checkInDate < :endDate " +
            "AND r.checkOutDate > :startDate")
    List<Reservation> findReservationsForDateRange(@Param("startDate") Date startDate,
                                                   @Param("endDate") Date endDate);
}
