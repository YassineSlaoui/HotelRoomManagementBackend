package com.ys.hotelroommanagementbackend.web;

import com.ys.hotelroommanagementbackend.dto.ReservationDTO;
import com.ys.hotelroommanagementbackend.mapper.ReservationMapper;
import com.ys.hotelroommanagementbackend.service.ReservationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/reservations")
@CrossOrigin("*")
@SecurityRequirement(name = "Authorization")
public class ReservationRestController {

    private final ReservationMapper reservationMapper;
    private final ReservationService reservationService;

    public ReservationRestController(ReservationService reservationService, ReservationMapper reservationMapper) {
        this.reservationService = reservationService;
        this.reservationMapper = reservationMapper;
    }

    @GetMapping("/{reservationId}")
    @PreAuthorize("hasAuthority('Admin')")
    public ReservationDTO getReservationById(@PathVariable Long reservationId) {
        return reservationMapper.fromReservation(reservationService.getReservationById(reservationId));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('Admin')")
    public Page<ReservationDTO> getAllReservations(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        return reservationService.getAllReservations(page, size);
    }

    @GetMapping("/guest/{guestId}")
    @PreAuthorize("hasAuthority('Admin') or @securityUtil.isGuestOwner(#guestId)")
    public Page<ReservationDTO> getReservationsByGuest(@PathVariable Long guestId,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        return reservationService.getReservationsByGuest(guestId, page, size);
    }

    @GetMapping("/room/{roomId}")
    @PreAuthorize("hasAuthority('Admin')")
    public Page<ReservationDTO> getReservationsByRoom(@PathVariable Long roomId,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        return reservationService.getReservationsByRoom(roomId, page, size);
    }

    @GetMapping("/endOn/{endDate}")
    @PreAuthorize("hasAuthority('Admin')")
    public Page<ReservationDTO> getReservationsEndingOn(@PathVariable Date endDate,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        return reservationService.getReservationsEndingOn(endDate, page, size);
    }

    @GetMapping("/startOn/{startDate}")
    @PreAuthorize("hasAuthority('Admin')")
    public Page<ReservationDTO> getReservationsStartingOn(@PathVariable Date startDate,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        return reservationService.getReservationsStartingOn(startDate, page, size);
    }

    @GetMapping("/startFrom/{startDate}")
    @PreAuthorize("hasAuthority('Admin')")
    public Page<ReservationDTO> getReservationsStartingFrom(@PathVariable Date startDate,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        return reservationService.getReservationsStartingFrom(startDate, page, size);
    }

    @GetMapping("/guestRoom/{guestId}/{roomId}/{checkInDate}")
    @PreAuthorize("hasAuthority('Admin') or @securityUtil.isGuestOwner(#guestId)")
    public ReservationDTO getReservationByGuestAndRoomAndCheckInDate(@PathVariable Long guestId,
                                                                     @PathVariable Long roomId,
                                                                     @PathVariable Date checkInDate) {
        return reservationService.getReservationByGuestAndRoomAndCheckInDate(guestId, roomId, checkInDate);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Admin') or @securityUtil.isGuestOwner(#reservationDTO.guest.guestId)")
    public ReservationDTO createReservation(@RequestBody ReservationDTO reservationDTO) {
        return reservationService.createReservation(reservationDTO);
    }

    @PutMapping("/{reservationId}")
    @PreAuthorize("hasAuthority('Admin') or @securityUtil.isGuestOwner(#reservationDTO.guest.guestId)")
    public ReservationDTO updateReservation(@PathVariable Long reservationId,
                                            @RequestBody ReservationDTO reservationDTO) {
        reservationDTO.setReservationId(reservationId);
        return reservationService.updateReservation(reservationDTO);
    }

    @DeleteMapping("/{reservationId}")
    @PreAuthorize("hasAuthority('Admin')")
    public void deleteReservation(@PathVariable Long reservationId) {
        reservationService.deleteReservation(reservationId);
    }
}
