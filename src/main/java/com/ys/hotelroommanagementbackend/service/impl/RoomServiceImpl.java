package com.ys.hotelroommanagementbackend.service.impl;

import com.ys.hotelroommanagementbackend.dao.ReservationDao;
import com.ys.hotelroommanagementbackend.dao.RoomDao;
import com.ys.hotelroommanagementbackend.dto.FilterDTO;
import com.ys.hotelroommanagementbackend.dto.GuestDTO;
import com.ys.hotelroommanagementbackend.dto.ReservationDTO;
import com.ys.hotelroommanagementbackend.dto.RoomDTO;
import com.ys.hotelroommanagementbackend.entity.Reservation;
import com.ys.hotelroommanagementbackend.entity.Room;
import com.ys.hotelroommanagementbackend.mapper.ReservationMapper;
import com.ys.hotelroommanagementbackend.mapper.RoomMapper;
import com.ys.hotelroommanagementbackend.service.ReservationService;
import com.ys.hotelroommanagementbackend.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@EnableScheduling
public class RoomServiceImpl implements RoomService {

    private ReservationMapper reservationMapper;
    private ReservationDao reservationDao;
    private RoomDao roomDao;

    private RoomMapper roomMapper;

    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public RoomServiceImpl(RoomDao roomDao, RoomMapper roomMapper, ReservationDao reservationDao, ReservationMapper reservationMapper) {
        this.roomDao = roomDao;
        this.roomMapper = roomMapper;
        this.reservationDao = reservationDao;
        this.reservationMapper = reservationMapper;
    }

    @Override
    public Room getRoomById(Long roomId) {
        return roomDao.findById(roomId).orElseThrow(() -> new RuntimeException("Room with id " + roomId + " not found"));
    }

    @Override
    public Page<RoomDTO> getAllRooms(int page, int size) {
        return roomDao.findAll(PageRequest.of(page, size, Sort.by("roomNumber").descending()))
                .map(room -> roomMapper.fromRoom(room));
    }

    @Override
    public Page<RoomDTO> getRoomsByFilters(List<FilterDTO> filters, int page, int size) {
        String roomNumber = null;
        String roomType = null;
        String description = null;
        Double minPrice = null;
        Double maxPrice = null;
        Integer minCapacity = null;
        Integer maxCapacity = null;
        Boolean available = null;
        Integer minRating = null;

        for (FilterDTO filter : filters) {
            switch (filter.getFilterName()) {
                case "roomNumber":
                    roomNumber = (String) filter.getFilterValue();
                    break;
                case "roomType":
                    roomType = (String) filter.getFilterValue();
                    break;
                case "description":
                    description = (String) filter.getFilterValue();
                    break;
                case "minPrice":
                    minPrice = (Double) filter.getFilterValue();
                    break;
                case "maxPrice":
                    maxPrice = (Double) filter.getFilterValue();
                    break;
                case "minCapacity":
                    minCapacity = (Integer) filter.getFilterValue();
                    break;
                case "maxCapacity":
                    maxCapacity = (Integer) filter.getFilterValue();
                    break;
                case "available":
                    available = (Boolean) filter.getFilterValue();
                    break;
                case "minRating":
                    minRating = (Integer) filter.getFilterValue();
                    break;
                default:
                    logger.warn("[ getRoomsByFilters() ] Invalid filter field: {}", filter.getFilterName());
                    break;
            }
        }

        return roomDao.getRoomsByFilters(roomNumber, roomType, description,
                        minPrice, maxPrice, minCapacity, maxCapacity, available, minRating,
                        PageRequest.of(page, size))
                .map(room -> roomMapper.fromRoom(room));
    }

    @Override
    public Page<RoomDTO> getNewRoomsForGuest(Long guestId, int page, int size) {
        return roomDao.findNewRoomsForGuest(guestId, PageRequest.of(page, size))
                .map(room -> roomMapper.fromRoom(room));
    }

    @Override
    public Page<RoomDTO> getPreviouslyBookedRoomsForGuest(Long guestId, int page, int size) {
        return roomDao.findPreviouslyBookedRoomsForGuest(guestId, PageRequest.of(page, size))
                .map(room -> roomMapper.fromRoom(room));
    }

    @Override
    public List<Room> getAllRooms() {
        return roomDao.findAll();
    }

    @Override
    public RoomDTO createRoom(RoomDTO roomDTO) {
        Room roomToBeSaved = roomMapper.toRoom(roomDTO);
        Room savedRoom = roomDao.save(roomToBeSaved);
        return roomMapper.fromRoom(savedRoom);
    }

    @Override
    public RoomDTO updateRoom(RoomDTO roomDTO) {
        Room roomToBeUpdated = roomMapper.toRoom(roomDTO);
        Room savedRoom = getRoomById(roomToBeUpdated.getRoomId());
        roomToBeUpdated.setReservations(savedRoom.getReservations());
        Room updatedRoom = roomDao.save(roomToBeUpdated);
        return roomMapper.fromRoom(updatedRoom);
    }

    @Override
    public void deleteRoom(Long roomId) {
        List<ReservationDTO> currentOrFutureReservations = reservationDao
                .findReservationsByCheckInDateAfterOrCheckOutDateAfter(new Date(), new Date())
                .stream().map(reservationMapper::fromReservation).toList();
        if (!currentOrFutureReservations.isEmpty())
            throw new RuntimeException("Trying to delete a room that has ongoing and/or future reservations, reservations: " + currentOrFutureReservations);
        roomDao.deleteById(roomId);
    }
}
