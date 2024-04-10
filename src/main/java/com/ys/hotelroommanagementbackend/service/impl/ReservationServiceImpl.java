package com.ys.hotelroommanagementbackend.service.impl;

import com.ys.hotelroommanagementbackend.dao.ReservationDao;
import com.ys.hotelroommanagementbackend.dto.ReservationDTO;
import com.ys.hotelroommanagementbackend.entity.Reservation;
import com.ys.hotelroommanagementbackend.entity.Room;
import com.ys.hotelroommanagementbackend.mapper.ReservationMapper;
import com.ys.hotelroommanagementbackend.mapper.RoomMapper;
import com.ys.hotelroommanagementbackend.service.GuestService;
import com.ys.hotelroommanagementbackend.service.ReservationService;
import com.ys.hotelroommanagementbackend.service.ReviewService;
import com.ys.hotelroommanagementbackend.service.RoomService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private ReservationDao reservationDao;
    private ReservationMapper reservationMapper;
    private GuestService guestService;
    private RoomService roomService;
    private ReviewService reviewService;
    private RoomMapper roomMapper;

    public ReservationServiceImpl(ReservationDao reservationDao, ReservationMapper reservationMapper, GuestService guestService, RoomService roomService, ReviewService reviewService, RoomMapper roomMapper) {
        this.reservationDao = reservationDao;
        this.reservationMapper = reservationMapper;
        this.guestService = guestService;
        this.roomService = roomService;
        this.reviewService = reviewService;
        this.roomMapper = roomMapper;
    }

    @Override
    public Reservation getReservationById(Long reservationId) {
        return reservationDao.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation with id " + reservationId + " not found"));
    }

    @Override
    public Page<ReservationDTO> getAllReservations(int page, int size) {
        return reservationDao.findAll(PageRequest.of(page, size))
                .map(reservation -> reservationMapper.fromReservation(reservation));
    }

    @Override
    public Page<ReservationDTO> getReservationsByGuestAndRoom(Long guestId, Long roomId, int page, int size) {
        return reservationDao.findReservationsByGuestAndRoom(guestService.getGuestById(guestId),
                        roomService.getRoomById(roomId), PageRequest.of(page, size))
                .map(reservation -> reservationMapper.fromReservation(reservation));
    }

    @Override
    public Page<ReservationDTO> getReservationsByGuest(Long guestId, int page, int size) {
        return reservationDao.findReservationsByGuest(guestService.getGuestById(guestId), PageRequest.of(page, size))
                .map(reservation -> reservationMapper.fromReservation(reservation));
    }

    @Override
    public Page<ReservationDTO> getReservationsByRoom(Long roomId, int page, int size) {
        return reservationDao.findReservationsByRoom(roomService.getRoomById(roomId), PageRequest.of(page, size))
                .map(reservation -> reservationMapper.fromReservation(reservation));
    }

    @Override
    public Page<ReservationDTO> getReservationsEndingOn(Date endDate, int page, int size) {
        return reservationDao.findReservationsByCheckOutDate(endDate, PageRequest.of(page, size))
                .map(reservation -> reservationMapper.fromReservation(reservation));
    }

    @Override
    public Page<ReservationDTO> getReservationsStartingOn(Date startDate, int page, int size) {
        return reservationDao.findReservationsByCheckInDate(startDate, PageRequest.of(page, size))
                .map(reservation -> reservationMapper.fromReservation(reservation));
    }

    @Override
    public Page<ReservationDTO> getReservationsStartingFrom(Date startDate, int page, int size) {
        return reservationDao.findReservationsByCheckInDateAfter(startDate, PageRequest.of(page, size))
                .map(reservation -> reservationMapper.fromReservation(reservation));
    }

    @Override
    public ReservationDTO getReservationByGuestAndRoomAndCheckInDate(Long guestId, Long roomId, Date checkInDate) {
        return reservationMapper.fromReservation(reservationDao
                .findReservationByGuestAndRoomAndCheckInDate(guestService.getGuestById(guestId),
                        roomService.getRoomById(roomId), checkInDate)
                .orElseThrow(() -> new RuntimeException("Reservation with guestId: " + guestId + ", roomId: " + roomId +
                        ", and checkInDate: " + checkInDate + " not found")));
    }

    @Override
    public ReservationDTO createReservation(ReservationDTO reservationDTO) {
        Reservation reservationToBeSaved = reservationMapper.toReservation(reservationDTO);
        reservationToBeSaved.setGuest(guestService.getGuestById(reservationDTO.getGuest().getGuestId()));
        Room roomToBook = roomService.getRoomById(reservationDTO.getRoom().getRoomId());
        reservationToBeSaved.setRoom(roomToBook);
        Reservation savedReservation = reservationDao.save(reservationToBeSaved);
        return reservationMapper.fromReservation(savedReservation);
    }

    @Override
    public ReservationDTO updateReservation(ReservationDTO reservationDTO) {
        Reservation reservationToBeUpdated = getReservationById(reservationDTO.getReservationId());
        reservationToBeUpdated.setGuest(guestService.getGuestById(reservationDTO.getGuest().getGuestId()));
        reservationToBeUpdated.setRoom(roomService.getRoomById(reservationDTO.getRoom().getRoomId()));
        reservationToBeUpdated.setReview(reviewService.getReviewById(reservationDTO.getReview().getReviewId()));
        Reservation updatedReservation = reservationDao.save(reservationToBeUpdated);
        return reservationMapper.fromReservation(updatedReservation);
    }

    @Override
    public void deleteReservation(Long reservationId) {
        reservationDao.deleteById(reservationId);
    }

    @Override
    public void addReviewToReservation(Long reservationId, Long reviewId) {
        Reservation reservation = getReservationById(reservationId);
        reservation.setReview(reviewService.getReviewById(reviewId));
        reservationDao.save(reservation);
    }

    @Override
    public boolean isRoomAvailable(Long roomId, Date checkInDate, Date checkOutDate) {
        return reservationDao.isRoomReserved(roomService.getRoomById(roomId), checkInDate, checkOutDate);
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Override
    public void updateRoomsAvailability() {
        Date currentDate = new Date();
        Date tomorrowDate = DateUtils.addDays(currentDate, 1);

        List<Reservation> reservations = reservationDao.findReservationsForDateRange(currentDate, tomorrowDate);

        Map<Long, List<Reservation>> reservationsByRoom = reservations.stream()
                .collect(Collectors.groupingBy(reservation -> reservation.getRoom().getRoomId()));

        List<Room> rooms = roomService.getAllRooms();

        for (Room room : rooms) {
            List<Reservation> roomReservations = reservationsByRoom.getOrDefault(room.getRoomId(), Collections.emptyList());
            if (!room.getAvailable().equals(roomReservations.isEmpty())) {
                room.setAvailable(!room.getAvailable());
                roomService.updateRoom(roomMapper.fromRoom(room));
            }
        }
    }
}
