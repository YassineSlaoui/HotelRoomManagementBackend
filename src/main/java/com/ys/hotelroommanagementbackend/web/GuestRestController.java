package com.ys.hotelroommanagementbackend.web;

import com.ys.hotelroommanagementbackend.dto.GuestDTO;
import com.ys.hotelroommanagementbackend.service.GuestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/guests")
@CrossOrigin("*")
@SecurityRequirement(name = "Access Token Authorization")
public class GuestRestController {

    private final GuestService guestService;

    public GuestRestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @Operation(summary = "Search guests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Guests found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping
    @PreAuthorize("hasAuthority('Admin')")
    public Page<GuestDTO> searchGuests(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                                       @RequestParam(name = "page", defaultValue = "0") int page,
                                       @RequestParam(name = "size", defaultValue = "5") int size) {
        return guestService.getGuestsByKeyword(keyword, page, size);
    }

    @Operation(summary = "Create a guest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Guest created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('Admin')")
    public GuestDTO createGuest(@RequestBody GuestDTO guestDTO) {
        return guestService.createGuest(guestDTO);
    }

    @Operation(summary = "Update a guest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Guest updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PutMapping("/{guestId}")
    @PreAuthorize("hasAuthority('Admin') or @securityUtil.isGuestOwner(#guestId)")
    public GuestDTO updateGuest(@RequestBody GuestDTO guestDTO, @PathVariable long guestId) {
        guestDTO.setGuestId(guestId);
        return guestService.updateGuest(guestDTO);
    }

    @Operation(summary = "Delete a guest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Guest deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
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
