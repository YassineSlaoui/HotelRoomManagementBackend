package com.ys.hotelroommanagementbackend.dao;

import com.ys.hotelroommanagementbackend.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface RoomDao extends JpaRepository<Room, Long> {
    @Query("SELECT r FROM Room AS r WHERE " +
            "(:roomNumber IS NULL OR r.roomNumber = :roomNumber) AND " +
            "(:roomType IS NULL OR r.roomType = :roomType) AND " +
            "(:description IS NULL OR LOWER(r.description) LIKE CONCAT('%', LOWER(:description), '%')) AND " +
            "(:minPrice IS NULL OR r.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR r.price <= :maxPrice) AND " +
            "(:minCapacity IS NULL OR r.capacity >= :minCapacity) AND " +
            "(:maxCapacity IS NULL OR r.capacity <= :maxCapacity) AND " +
            "(:available IS NULL OR r.available = :available) AND " +
            "(:minRating IS NULL OR NOT EXISTS (SELECT rv FROM Review AS rv WHERE rv.reservation.room = r AND rv.rating < :minRating)) AND " +
            "(:availabilityStartDate IS NULL OR r.availabilityStartDate >= :availabilityStartDate) AND " +
            "(:availabilityEndDate IS NULL OR r.availabilityEndDate <= :availabilityEndDate)")
    List<Room> filterRooms(@Param("roomNumber") String roomNumber,
                           @Param("roomType") String roomType,
                           @Param("description") String description,
                           @Param("minPrice") Double minPrice,
                           @Param("maxPrice") Double maxPrice,
                           @Param("minCapacity") Integer minCapacity,
                           @Param("maxCapacity") Integer maxCapacity,
                           @Param("available") Boolean available,
                           @Param("minRating") Integer minRating,
                           @Param("availabilityStartDate") Date availabilityStartDate,
                           @Param("availabilityEndDate") Date availabilityEndDate);
}
