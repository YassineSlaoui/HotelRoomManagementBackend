package com.ys.hotelroommanagementbackend.web;

import com.ys.hotelroommanagementbackend.dto.GuestDTO;
import com.ys.hotelroommanagementbackend.service.GuestService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/guests")
@CrossOrigin("*")
public class GuestRestController {

    private final GuestService guestService;

    public GuestRestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('Admin')")
    public Page<GuestDTO> searchGuests(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                                       @RequestParam(name = "page", defaultValue = "0") int page,
                                       @RequestParam(name = "size", defaultValue = "5") int size) {
        return guestService.getGuestsByKeyword(keyword, page, size);
    }

    @PostMapping
    @PreAuthorize("permitAll()")
    public GuestDTO createGuest(@RequestBody GuestDTO guestDTO) {
        return guestService.createGuest(guestDTO);
    }

    @PutMapping("/{guestId}")
    @PreAuthorize("hasAuthority('Admin') or @securityUtil.isGuestOwner(#guestId)")
    public GuestDTO updateGuest(@RequestBody GuestDTO guestDTO, @PathVariable long guestId) {
        guestDTO.setGuestId(guestId);
        return guestService.updateGuest(guestDTO);
    }

    @DeleteMapping("/{guestId}")
    @PreAuthorize("hasAuthority('Admin') or @securityUtil.isGuestOwner(#guestId)")
    public void deleteGuest(@PathVariable Long guestId, HttpServletRequest request, HttpServletResponse response) {
        guestService.deleteGuest(guestId);

        if (!SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .toList().contains("Admin")) {
            SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
            logoutHandler.logout(request, response, null);
        }
    }
}
