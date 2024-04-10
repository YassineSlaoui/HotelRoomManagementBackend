package com.ys.hotelroommanagementbackend.runner;

import com.ys.hotelroommanagementbackend.dto.GuestDTO;
import com.ys.hotelroommanagementbackend.dto.ReservationDTO;
import com.ys.hotelroommanagementbackend.dto.RoomDTO;
import com.ys.hotelroommanagementbackend.dto.UserDTO;
import com.ys.hotelroommanagementbackend.service.*;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class CLIRunner implements CommandLineRunner {

    RoleService roleService;
    UserService userService;
    GuestService guestService;
    RoomService roomService;
    ReservationService reservationService;
    ReviewService reviewService;
    List<GuestDTO> guestDTOS = new ArrayList<>();
    List<RoomDTO> roomDTOS = new ArrayList<>();

    public CLIRunner(RoleService roleService, UserService userService, GuestService guestService, RoomService roomService, ReservationService reservationService, ReviewService reviewService) {
        this.roleService = roleService;
        this.userService = userService;
        this.guestService = guestService;
        this.roomService = roomService;
        this.reservationService = reservationService;
        this.reviewService = reviewService;
    }

    @Override
    public void run(String... args) throws Exception {
        createRoles();
        createAdmin();
        createGuests();
        createRooms();
        createReservations();
    }

    private void createRoles() {
        Arrays.asList("Admin", "Guest").forEach(roleService::createRole);
    }

    private void createAdmin() {
        userService.createUser("admin", "admin@hotel-domain.com", "1234");
        userService.assignRoleToUser("admin@hotel-domain.com", "admin");
    }

    private void createGuests() {
        for (int i = 1; i < 11; i++) {
            GuestDTO guestDTO = GuestDTO.builder()
                    .firstName("Guest" + i + "'s First Name")
                    .lastName("Guest" + i + "'s Last Name")
                    .contactInfo("Guest" + i + "'s Contact Info")
                    .user(UserDTO.builder()
                            .username("Guest" + i)
                            .email("Guest" + i + "@hotel-domain.com")
                            .password("1234")
                            .build())
                    .build();
            GuestDTO savedGuestDTO = guestService.createGuest(guestDTO);
            userService.assignRoleToUser("Guest" + i + "@hotel-domain.com", "guest");
            guestDTOS.add(savedGuestDTO);
        }
    }

    private void createRooms() {
        for (int i = 1; i < 11; i++) {
            RoomDTO roomDTO = RoomDTO.builder()
                    .roomNumber("Room" + i)
                    .price((double) (i * 10))
                    .roomType(i % 3 == 0 ? "Single Room" : i % 3 == 1 ? "Double Room" : "Triple Room")
                    .capacity(i % 3 == 0 ? 1 : i % 3 == 1 ? 2 : 3)
                    .description("A good room, like all the other rooms!")
                    .available(true)
                    .build();
            roomDTO = roomService.createRoom(roomDTO);
            roomDTOS.add(roomDTO);
        }
    }

    private void createReservations() {
        reservationService.createReservation(ReservationDTO.builder()
                .room(roomDTOS.get(1))
                .guest(guestDTOS.get(2))
                .checkInDate(DateUtils.addMinutes(new Date(), 1))
                .checkOutDate(DateUtils.addMinutes(new Date(), 2))
                .build());

        reservationService.createReservation(ReservationDTO.builder()
                .room(roomDTOS.get(2))
                .guest(guestDTOS.get(5))
                .checkInDate(DateUtils.addMinutes(new Date(), 1))
                .checkOutDate(DateUtils.addMinutes(new Date(), 3))
                .build());
    }
}
