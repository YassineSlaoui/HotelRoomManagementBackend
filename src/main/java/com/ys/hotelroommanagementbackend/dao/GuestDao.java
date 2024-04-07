package com.ys.hotelroommanagementbackend.dao;

import com.ys.hotelroommanagementbackend.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GuestDao extends JpaRepository<Guest, Long> {

    @Query("SELECT g FROM Guest AS g WHERE " +
            "LOWER(g.firstName) LIKE CONCAT('%', LOWER(:name), '%') OR " +
            "LOWER(g.lastName) LIKE CONCAT('%', LOWER(:name), '%')")
    List<Guest> findGuestByName(@Param("name") String name);

    @Query("SELECT g FROM Guest AS g WHERE " +
            "g.user.email = :email")
    Optional<Guest> findGuestByEmail(@Param("email") String email);

    @Query("SELECT g FROM Guest AS g WHERE " +
            "g.user.username = :username")
    Optional<Guest> findGuestByUsername(@Param("username") String username);
}
