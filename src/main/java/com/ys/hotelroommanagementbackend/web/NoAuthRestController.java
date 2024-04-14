package com.ys.hotelroommanagementbackend.web;

import com.ys.hotelroommanagementbackend.dto.GuestDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/noauth")
@CrossOrigin("*")
public class NoAuthRestController {

    private final GuestRestController guestRestController;
    private final UserRestController userRestController;

    public NoAuthRestController(GuestRestController guestRestController, UserRestController userRestController) {
        this.guestRestController = guestRestController;
        this.userRestController = userRestController;
    }

    @PostMapping("/signup/guest")
    public GuestDTO createGuest(@RequestBody GuestDTO guestDTO) {
        return guestRestController.createGuest(guestDTO);
    }

    @GetMapping("/refresh-token")
    public void redirectToUserRestController(HttpServletRequest request, HttpServletResponse response) throws IOException {
        userRestController.generateNewAccessToken(request, response);
    }
}
