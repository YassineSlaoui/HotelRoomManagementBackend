package com.ys.hotelroommanagementbackend.service.impl;

import com.ys.hotelroommanagementbackend.dao.ReservationDao;
import com.ys.hotelroommanagementbackend.dto.ReservationDTO;
import com.ys.hotelroommanagementbackend.entity.Reservation;
import com.ys.hotelroommanagementbackend.mapper.ReservationMapper;
import com.ys.hotelroommanagementbackend.service.GuestService;
import com.ys.hotelroommanagementbackend.service.ReservationService;
import com.ys.hotelroommanagementbackend.service.ReviewService;
import com.ys.hotelroommanagementbackend.service.RoomService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private ReservationDao reservationDao;
    private ReservationMapper reservationMapper;
    private GuestService guestService;
    private RoomService roomService;
    private ReviewService reviewService;

    public ReservationServiceImpl(ReservationDao reservationDao, ReservationMapper reservationMapper, GuestService guestService, RoomService roomService, ReviewService reviewService) {
        this.reservationDao = reservationDao;
        this.reservationMapper = reservationMapper;
        this.guestService = guestService;
        this.roomService = roomService;
        this.reviewService = reviewService;
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
        reservationToBeSaved.setRoom(roomService.getRoomById(reservationDTO.getRoom().getRoomId()));
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
}
