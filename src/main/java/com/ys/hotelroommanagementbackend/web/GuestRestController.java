package com.ys.hotelroommanagementbackend.web;

import com.ys.hotelroommanagementbackend.dto.GuestDTO;
import com.ys.hotelroommanagementbackend.service.GuestService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/guests")
public class GuestRestController {

    private GuestService guestService;

    public GuestRestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @GetMapping
    public Page<GuestDTO> searchGuests(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                                       @RequestParam(name = "page", defaultValue = "0") int page,
                                       @RequestParam(name = "size", defaultValue = "5") int size) {
        return guestService.getGuestsByKeyword(keyword, page, size);
    }

    @PostMapping
    public GuestDTO saveGuest(@RequestBody GuestDTO guestDTO) {
        return guestService.createGuest(guestDTO);
    }

    @PutMapping("/{guestId}")
    public GuestDTO updateGuest(@RequestBody GuestDTO guestDTO, @PathVariable long guestId) {
        guestDTO.setGuestId(guestId);
        return guestService.updateGuest(guestDTO);
    }

    @DeleteMapping("/{guestId}")
    public void deleteGuest(@PathVariable long guestId) {
        guestService.deleteGuest(guestId);
    }
}
