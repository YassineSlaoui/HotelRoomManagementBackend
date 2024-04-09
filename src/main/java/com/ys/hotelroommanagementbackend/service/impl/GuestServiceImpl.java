package com.ys.hotelroommanagementbackend.service.impl;

import com.ys.hotelroommanagementbackend.dao.GuestDao;
import com.ys.hotelroommanagementbackend.dto.GuestDTO;
import com.ys.hotelroommanagementbackend.entity.Guest;
import com.ys.hotelroommanagementbackend.entity.Reservation;
import com.ys.hotelroommanagementbackend.entity.User;
import com.ys.hotelroommanagementbackend.mapper.GuestMapper;
import com.ys.hotelroommanagementbackend.service.GuestService;
import com.ys.hotelroommanagementbackend.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GuestServiceImpl implements GuestService {

    private GuestDao guestDao;

    private GuestMapper guestMapper;

    private UserService userService;

    public GuestServiceImpl(GuestDao guestDao, GuestMapper guestMapper, UserService userService) {
        this.guestDao = guestDao;
        this.guestMapper = guestMapper;
        this.userService = userService;
    }

    @Override
    public List<GuestDTO> getAllGuests() {
        return guestDao.findAll().stream().map(guest -> guestMapper.fromGuest(guest)).toList();
    }

    @Override
    public Page<GuestDTO> getGuestsByName(String name, int page, int size) {
        return guestDao.findGuestByName(name, PageRequest.of(page, size)).map(guest -> guestMapper.fromGuest(guest));
    }

    @Override
    public Page<GuestDTO> getAllGuests(int page, int size) {
        return guestDao.findAll(PageRequest.of(page, size)).map(guest -> guestMapper.fromGuest(guest));
    }

    @Override
    public GuestDTO getGuestByEmail(String email) {
        return guestMapper.fromGuest(guestDao.findGuestByEmail(email)
                .orElseThrow(() -> new RuntimeException("Guest with email " + email + " not found")));
    }

    @Override
    public GuestDTO getGuestByUsername(String username) {
        return guestMapper.fromGuest(guestDao.findGuestByUsername(username)
                .orElseThrow(() -> new RuntimeException("Guest with username " + username + " not found")));
    }

    @Override
    public GuestDTO createGuest(GuestDTO guestDTO) {
        User newGuestUser = userService.createUser(guestDTO.getUser().getUsername(),
                guestDTO.getUser().getEmail(), guestDTO.getUser().getPassword());
        userService.assignRoleToUser(newGuestUser.getEmail(), "Guest");
        Guest guestToBeSaved = guestMapper.toGuest(guestDTO);
        guestToBeSaved.setUser(newGuestUser);
        Guest savedGuest = guestDao.save(guestToBeSaved);
        return guestMapper.fromGuest(savedGuest);
    }

    @Override
    public GuestDTO updateGuest(GuestDTO guestDTO) {
        Guest loadedGuest = getGuestById(guestDTO.getGuestId());
        Guest guestToUpdate = guestMapper.toGuest(guestDTO);
        guestToUpdate.setUser(loadedGuest.getUser());
        guestToUpdate.setReservations(loadedGuest.getReservations());
        Guest updatedGuest = guestDao.save(guestToUpdate);
        return guestMapper.fromGuest(updatedGuest);
    }

    @Override
    public Guest getGuestById(Long guestId) {
        return guestDao.findGuestByGuestId(guestId)
                .orElseThrow(() -> new RuntimeException("Guest with id " + guestId + " not found"));
    }

    @Override
    public void deleteGuest(Long guestId) {
        Guest guestToDelete = getGuestById(guestId);
        for (Reservation reservation : guestToDelete.getReservations())
            reservation.setGuest(null);
        guestDao.delete(guestToDelete);
    }
}
