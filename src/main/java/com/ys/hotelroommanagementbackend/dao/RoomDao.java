package com.ys.hotelroommanagementbackend.dao;

import com.ys.hotelroommanagementbackend.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomDao extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {

    @Query("SELECT DISTINCT r FROM Room r " +
            "LEFT JOIN r.reservations res " +
            "WHERE res.guest.guestId <> :guestId OR res IS NULL")
    Page<Room> findNewRoomsForGuest(@Param("guestId") Long guestId, Pageable pageable);

    @Query("SELECT DISTINCT r FROM Room r " +
            "JOIN r.reservations res " +
            "WHERE res.guest.guestId = :guestId")
    Page<Room> findPreviouslyBookedRoomsForGuest(@Param("guestId") Long guestId, Pageable pageable);

    /**
     * This method returns rooms that fit the criteria passed to it
     * <br>
     * The criteria parameters can be null, that way the rooms
     * are not filtered on them.
     *
     * @param roomNumber This will filter for rooms that have a room number like this
     * @param roomType This will filter for rooms with this room type
     * @param description This will filter for rooms that have a similar description to this
     * @param minPrice This will filter with rooms with price higher than this
     * @param maxPrice This will filter with rooms with price lower than this
     * @param minCapacity This will filter with rooms with capacity higher than this
     * @param maxCapacity This will filter with rooms with capacity lower than this
     * @param available This will filter rooms for availability
     * @param minRating This will filter for rooms with rating that's higher than this
     * @return Page of rooms that fit all the non-null criteria
     *
     * @author Yassine Slaoui
     */
    @Query("SELECT r FROM Room AS r " +
            "WHERE (:roomNumber IS NULL OR LOWER(r.roomNumber) LIKE CONCAT('%', LOWER(:roomNumber), '%')) " +
            "AND (:roomType IS NULL OR r.roomType = :roomType) " +
            "AND (:description IS NULL OR LOWER(r.description) LIKE CONCAT('%', LOWER(:description), '%')) " +
            "AND (:minPrice IS NULL OR r.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR r.price <= :maxPrice) " +
            "AND (:minCapacity IS NULL OR r.capacity >= :minCapacity) " +
            "AND (:maxCapacity IS NULL OR r.capacity <= :maxCapacity) " +
            "AND (:available IS NULL OR r.available = :available) " +
            "AND (:minRating IS NULL OR NOT EXISTS (SELECT rv FROM Review AS rv WHERE rv.reservation.room = r AND rv.rating < :minRating))")
    Page<Room> getRoomsByFilters(@Param("roomNumber") String roomNumber,
                                 @Param("roomType") String roomType,
                                 @Param("description") String description,
                                 @Param("minPrice") Double minPrice,
                                 @Param("maxPrice") Double maxPrice,
                                 @Param("minCapacity") Integer minCapacity,
                                 @Param("maxCapacity") Integer maxCapacity,
                                 @Param("available") Boolean available,
                                 @Param("minRating") Integer minRating,
                                 Pageable pageable);
}
