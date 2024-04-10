package com.ys.hotelroommanagementbackend.dao;

import com.ys.hotelroommanagementbackend.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewDao extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review AS r " +
            "WHERE r.reservation.room.roomId = :roomId")
    Page<Review> findReviewsByRoom(@Param("roomId") Long roomId, Pageable pageable);

    @Query("SELECT r FROM Review AS r " +
            "WHERE r.reservation.guest.guestId = :guestId")
    Page<Review> findReviewsByGuest(@Param("guestId") Long guestId, Pageable pageable);
}
