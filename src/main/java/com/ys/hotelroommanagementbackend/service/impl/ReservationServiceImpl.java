package com.ys.hotelroommanagementbackend.service.impl;

import com.ys.hotelroommanagementbackend.dao.ReservationDao;
import com.ys.hotelroommanagementbackend.dto.ReservationDTO;
import com.ys.hotelroommanagementbackend.dto.RoomDTO;
import com.ys.hotelroommanagementbackend.entity.Reservation;
import com.ys.hotelroommanagementbackend.entity.Room;
import com.ys.hotelroommanagementbackend.mapper.ReservationMapper;
import com.ys.hotelroommanagementbackend.mapper.RoomMapper;
import com.ys.hotelroommanagementbackend.service.GuestService;
import com.ys.hotelroommanagementbackend.service.ReservationService;
import com.ys.hotelroommanagementbackend.service.ReviewService;
import com.ys.hotelroommanagementbackend.service.RoomService;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.EnableScheduling;
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
@EnableScheduling
public class ReservationServiceImpl implements ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationMapper reservationMapper;
    private final GuestService guestService;
    private final RoomService roomService;
    private final ReviewService reviewService;
    private final RoomMapper roomMapper;

    final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

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
                .map(reservationMapper::fromReservation);
    }

    @Override
    public Page<ReservationDTO> getReservationsByGuestAndRoom(Long guestId, Long roomId, int page, int size) {
        return reservationDao.findReservationsByGuestAndRoom(guestService.getGuestById(guestId),
                        roomService.getRoomById(roomId), PageRequest.of(page, size))
                .map(reservationMapper::fromReservation);
    }

    @Override
    public Page<ReservationDTO> getReservationsByGuest(Long guestId, int page, int size) {
        return reservationDao.findReservationsByGuest(guestService.getGuestById(guestId), PageRequest.of(page, size))
                .map(reservationMapper::fromReservation);
    }

    @Override
    public Page<ReservationDTO> getReservationsByRoom(Long roomId, int page, int size) {
        return reservationDao.findReservationsByRoom(roomService.getRoomById(roomId), PageRequest.of(page, size))
                .map(reservationMapper::fromReservation);
    }

    @Override
    public Page<ReservationDTO> getReservationsEndingOn(Date endDate, int page, int size) {
        return reservationDao.findReservationsByCheckOutDate(endDate, PageRequest.of(page, size))
                .map(reservationMapper::fromReservation);
    }

    @Override
    public Page<ReservationDTO> getReservationsStartingOn(Date startDate, int page, int size) {
        return reservationDao.findReservationsByCheckInDate(startDate, PageRequest.of(page, size))
                .map(reservationMapper::fromReservation);
    }

    @Override
    public Page<ReservationDTO> getReservationsStartingFrom(Date startDate, int page, int size) {
        return reservationDao.findReservationsByCheckInDateAfter(startDate, PageRequest.of(page, size))
                .map(reservationMapper::fromReservation);
    }

    @Override
    public ReservationDTO getReservationByGuestAndRoomAndCheckInDate(Long guestId, Long roomId, Date checkInDate) {
        return reservationMapper.fromReservation(reservationDao
                .findReservationByGuestAndRoomAndCheckInDate(guestService.getGuestById(guestId),
                        roomService.getRoomById(roomId), checkInDate)
                .orElseThrow(() -> new RuntimeException("Reservation with guestId: " + guestId + ", roomId: " + roomId +
                        ", and checkInDate: " + checkInDate + " not found")));
    }

    private ReservationDTO checkAndSaveReservation(ReservationDTO reservationDTO) {
        Reservation reservationToBeSaved = reservationMapper.toReservation(reservationDTO);
        reservationToBeSaved.setGuest(guestService.getGuestById(reservationDTO.getGuest().getGuestId()));

        Room roomToBook = roomService.getRoomById(reservationDTO.getRoom().getRoomId());
        List<ReservationDTO> overlappingReservations = getOverlappingReservations(roomToBook.getRoomId(), reservationToBeSaved.getCheckInDate(), reservationToBeSaved.getCheckOutDate());

        if (!overlappingReservations.isEmpty())
            throw new RuntimeException("Room is not available, overlapping with reservations: " + overlappingReservations);
        if (roomToBook.getMaintenance())
            throw new RuntimeException("Room is under maintenance, cannot book it at the moment.");

        reservationToBeSaved.setRoom(roomToBook);

        if (reservationDTO.getReview() != null)
            reservationToBeSaved.setReview(reviewService.getReviewById(reservationDTO.getReview().getReviewId()));

        Reservation savedReservation = reservationDao.save(reservationToBeSaved);
        return reservationMapper.fromReservation(savedReservation);
    }

    @Override
    public ReservationDTO createReservation(ReservationDTO reservationDTO) {
        return checkAndSaveReservation(reservationDTO);
    }


    @Override
    public ReservationDTO updateReservation(ReservationDTO reservationDTO) {
        return checkAndSaveReservation(reservationDTO);
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
        return reservationDao.findOverlappingReservationsForRoom(roomService.getRoomById(roomId), checkInDate, checkOutDate).isEmpty();
    }

    @Override
    public List<ReservationDTO> getOverlappingReservations(Long roomId, Date checkInDate, Date checkOutDate) {
        return reservationDao.findOverlappingReservationsForRoom(roomService.getRoomById(roomId), checkInDate, checkOutDate).stream().map(reservationMapper::fromReservation).toList();
    }

    /**
     * This method will run At midnight, every day, to update the rooms availabilities.
     * <br>
     * We can make it @Scheduled(cron = "0 0 13 * * *") to run every day at 1 PM instead.
     *
     * @author Yassine Slaoui
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Override
    public void updateRoomsAvailability() {
        // Here I'm getting all today's reservations at once,
        // instead of getting each room's today's reservation on its own in the room's loop down below,
        // to lighten the load on the database.
        Date currentDate = new Date();
        Date tomorrowDate = DateUtils.addDays(currentDate, 1);

        List<ReservationDTO> reservations = reservationDao.findReservationsForDateRange(currentDate, tomorrowDate)
                .stream().map(reservationMapper::fromReservation).toList();

        Map<Long, List<ReservationDTO>> thisDaysReservationsByRoomMap =
                reservations.stream().collect(Collectors.groupingBy(reservation -> reservation.getRoom().getRoomId()));

        List<RoomDTO> rooms = roomService.getAllRooms();

        logger.info("[ Scheduled ] Checking reservations and rooms for availability updates");

        for (RoomDTO room : rooms) {
            if (room.getMaintenance()) {
                logger.info("[ Scheduled ] Room {} is under maintenance, it's not available.", room);
                room.setAvailable(false);
            } else {
                List<ReservationDTO> roomReservations = thisDaysReservationsByRoomMap.getOrDefault(room.getRoomId(), Collections.emptyList());
                if (!room.getAvailable().equals(roomReservations.isEmpty())) {
                    if (room.getAvailable())
                        logger.info("[ Scheduled ] Room {} was available yesterday, and now being set to unavailable starting this day, with reservation: {}",
                                room, roomReservations.getFirst());
                    else
                        logger.info("[ Scheduled ] Room {} was unavailable, and now being set to available",
                                room);
                    room.setAvailable(!room.getAvailable());
                    roomService.updateRoom(room);
                }
            }
        }
    }
}
