package com.ys.hotelroommanagementbackend.dao;

import com.ys.hotelroommanagementbackend.entity.Reservation;
import com.ys.hotelroommanagementbackend.entity.Review;
import com.ys.hotelroommanagementbackend.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewDao extends JpaRepository<Review, Long> {

    List<Review> findReviewsByReservation(Reservation reservation);

    @Query("SELECT r FROM Review AS r WHERE " +
            "r.reservation.room = :room")
    List<Review> findReviewsByRoom(@Param("room") Room room);
}
