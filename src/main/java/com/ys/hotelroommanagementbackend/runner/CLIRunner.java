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

@SuppressWarnings({"CommentedOutCode", "unused"})
@Component
public class CLIRunner implements CommandLineRunner {

    final RoleService roleService;
    final UserService userService;
    final GuestService guestService;
    final RoomService roomService;
    final ReservationService reservationService;
    final ReviewService reviewService;
    final List<GuestDTO> guestDTOS = new ArrayList<>();
    final List<RoomDTO> roomDTOS = new ArrayList<>();
    final List<ReservationDTO> reservationDTOS = new ArrayList<>();

    public CLIRunner(RoleService roleService, UserService userService, GuestService guestService, RoomService roomService, ReservationService reservationService, ReviewService reviewService) {
        this.roleService = roleService;
        this.userService = userService;
        this.guestService = guestService;
        this.roomService = roomService;
        this.reservationService = reservationService;
        this.reviewService = reviewService;
    }

    @Override
    public void run(String... args) {
        createRoles();
        createAdmin();
        createGuests();
        createRooms();
        createReservations();
        updateGuests();
        updateRooms();
        updateReservations();
//        removeGuests();
//        removeRooms();
//        removeReservations();
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
        reservationDTOS.add(reservationService.createReservation(ReservationDTO.builder()
                .room(roomDTOS.get(1))
                .guest(guestDTOS.get(2))
                .checkInDate(DateUtils.addMinutes(new Date(), 1))
                .checkOutDate(DateUtils.addMinutes(new Date(), 2))
                .build()));

        reservationDTOS.add(reservationService.createReservation(ReservationDTO.builder()
                .room(roomDTOS.get(2))
                .guest(guestDTOS.get(4))
                .checkInDate(DateUtils.addMinutes(new Date(), 1))
                .checkOutDate(DateUtils.addMinutes(new Date(), 3))
                .build()));
    }

    public void updateGuests() {
        GuestDTO guestToUpdate = guestDTOS.get(4);
        guestToUpdate.setFirstName("Guest5's Updated First Name");
        guestToUpdate.setLastName("Guest5's Updated Last Name");
        guestToUpdate.setContactInfo("Guest5's Updated Contact Info");
        UserDTO guestUserToUpdate = guestToUpdate.getUser();
        guestUserToUpdate.setUsername("updated_guest5");
        guestUserToUpdate.setEmail("updated_guest5@hotel-domain.com");
        guestUserToUpdate.setPassword("updated_1234");
        guestService.updateGuest(guestToUpdate);
    }

    public void updateRooms() {
        RoomDTO roomToUpdate = roomDTOS.get(4);
        roomToUpdate.setRoomNumber("Renovated Room 5");
        roomToUpdate.setRoomType("Suite");
        roomToUpdate.setDescription("Oh, this room isn't like the others.");
        roomToUpdate.setCapacity(10);
        roomToUpdate.setPrice(9999.99);
        roomService.updateRoom(roomToUpdate);
    }

    public void updateReservations() {
        ReservationDTO reservationToUpdate = reservationDTOS.get(1);
        reservationToUpdate.setCheckInDate(DateUtils.addDays(new Date(), 2));
        reservationToUpdate.setCheckOutDate(DateUtils.addDays(new Date(), 5));
        reservationToUpdate.setRoom(roomDTOS.get(4));
        reservationService.updateReservation(reservationToUpdate);

        reservationToUpdate = reservationDTOS.getFirst();
        reservationToUpdate.setCheckInDate(DateUtils.addDays(new Date(), 5));
        reservationToUpdate.setCheckOutDate(DateUtils.addDays(new Date(), 7));
        reservationToUpdate.setRoom(roomDTOS.get(4));
        reservationService.updateReservation(reservationToUpdate);
    }

    public void removeGuests() {
        guestService.deleteGuest(guestDTOS.get(4).getGuestId());
        guestDTOS.remove(guestDTOS.get(4));
    }

    public void removeRooms() {
        roomService.deleteRoom(roomDTOS.get(4).getRoomId());
        roomDTOS.remove(roomDTOS.getFirst());
    }

    public void removeReservations() {
        reservationService.deleteReservation(reservationDTOS.getFirst().getReservationId());
        reservationDTOS.remove(reservationDTOS.getFirst());
    }
}
