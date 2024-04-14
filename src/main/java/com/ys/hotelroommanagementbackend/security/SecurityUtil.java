package com.ys.hotelroommanagementbackend.security;

import com.ys.hotelroommanagementbackend.dto.GuestDTO;
import com.ys.hotelroommanagementbackend.entity.Guest;
import com.ys.hotelroommanagementbackend.entity.User;
import com.ys.hotelroommanagementbackend.service.GuestService;
import com.ys.hotelroommanagementbackend.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityUtil {

    GuestService guestService;
    UserService userService;

    public SecurityUtil(GuestService guestService, UserService userService) {
        this.guestService = guestService;
        this.userService = userService;
    }

    public boolean isGuestOwner(Long guestId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Guest guest = guestService.getGuestById(guestId);

        return authentication != null && (authentication.getName().equalsIgnoreCase(guest.getUser().getEmail()) ||
                authentication.getName().equalsIgnoreCase(guest.getUser().getUsername()));
    }

    public boolean isUserOwner(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.getUserById(userId);

        return authentication != null && (authentication.getName().equalsIgnoreCase(user.getEmail()) ||
                authentication.getName().equalsIgnoreCase(user.getUsername()));
    }

    public boolean isGuestReview(Long reviewId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        GuestDTO guestDTO = guestService.getGuestOfReview(reviewId);

        return authentication != null && (authentication.getName().equalsIgnoreCase(guestDTO.getUser().getEmail()) ||
                authentication.getName().equalsIgnoreCase(guestDTO.getUser().getUsername()));
    }
}
