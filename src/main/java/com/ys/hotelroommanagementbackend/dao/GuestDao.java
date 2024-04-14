package com.ys.hotelroommanagementbackend.dao;

import com.ys.hotelroommanagementbackend.entity.Guest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface GuestDao extends JpaRepository<Guest, Long> {

    @Query("SELECT g FROM Guest AS g " +
            "WHERE LOWER(g.firstName) LIKE CONCAT('%', LOWER(:keyword), '%') " +
            "OR LOWER(g.lastName) LIKE CONCAT('%', LOWER(:keyword), '%') "+
            "OR LOWER(g.contactInfo) LIKE CONCAT('%', LOWER(:keyword), '%') "+
            "OR LOWER(g.user.email) LIKE CONCAT('%', LOWER(:keyword), '%') "+
            "OR LOWER(g.user.username) LIKE CONCAT('%', LOWER(:keyword), '%')")
    Page<Guest> findGuestByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT g FROM Guest AS g " +
            "WHERE LOWER(g.firstName) LIKE CONCAT('%', LOWER(:name), '%') " +
            "OR LOWER(g.lastName) LIKE CONCAT('%', LOWER(:name), '%')")
    List<Guest> findGuestByKeyword(@Param("name") String name);

    @Query("SELECT g FROM Guest AS g " +
            "WHERE g.user.email = :email")
    Optional<Guest> findGuestByEmail(@Param("email") String email);

    @Query("SELECT g FROM Guest AS g " +
            "WHERE g.user.username = :username")
    Optional<Guest> findGuestByUsername(@Param("username") String username);

    Optional<Guest> findGuestByGuestId(Long guestId);

    @Query("SELECT g FROM Guest AS g " +
            "WHERE g IN " +
                "(SELECT r.guest FROM Reservation AS r " +
                "WHERE r.review = :reviewId)")
    Optional<Guest> findGuestByReviewId(Long reviewId);
}
