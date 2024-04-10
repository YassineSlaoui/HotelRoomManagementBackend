package com.ys.hotelroommanagementbackend.service;

import com.ys.hotelroommanagementbackend.dto.GuestDTO;
import com.ys.hotelroommanagementbackend.entity.Guest;
import org.springframework.data.domain.Page;

import java.util.List;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public interface GuestService {

    List<GuestDTO> getAllGuests();

    Page<GuestDTO> getGuestsByKeyword(String name, int page, int size);

    Page<GuestDTO> getAllGuests(int page, int size);

    GuestDTO getGuestByEmail(String email);

    GuestDTO getGuestByUsername(String username);

    GuestDTO createGuest(GuestDTO guestDTO);

    GuestDTO updateGuest(GuestDTO guestDTO);

    Guest getGuestById(Long guestId);

    void deleteGuest(Long guestId);
}
