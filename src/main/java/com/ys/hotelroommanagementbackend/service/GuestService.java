package com.ys.hotelroommanagementbackend.service;

import com.ys.hotelroommanagementbackend.dto.GuestDTO;
import com.ys.hotelroommanagementbackend.entity.Guest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GuestService {

    List<GuestDTO> getAllGuests();

    Page<GuestDTO> getGuestsByName(String name, int page, int size);

    Page<GuestDTO> fetchAllGuests(int page, int size);

    GuestDTO getGuestByEmail(String email);

    GuestDTO createGuest(GuestDTO guestDTO);

    GuestDTO updateGuest(GuestDTO guestDTO);

    Guest getGuestById(Long guestId);

    void deleteGuest(Long guestId);
}
