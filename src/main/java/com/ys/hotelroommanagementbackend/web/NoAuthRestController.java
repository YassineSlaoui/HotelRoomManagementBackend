package com.ys.hotelroommanagementbackend.web;

import com.ys.hotelroommanagementbackend.dto.CredentialsDTO;
import com.ys.hotelroommanagementbackend.dto.GuestDTO;
import com.ys.hotelroommanagementbackend.dto.JWTTokensDTO;
import com.ys.hotelroommanagementbackend.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/noauth")
@CrossOrigin("*")
@SecurityRequirement(name = "")
public class NoAuthRestController {

    private final GuestRestController guestRestController;
    private final UserRestController userRestController;
    private final AuthService authService;


    public NoAuthRestController(GuestRestController guestRestController, UserRestController userRestController, AuthService authService) {
        this.guestRestController = guestRestController;
        this.userRestController = userRestController;
        this.authService = authService;
    }

    @PostMapping("/signup/guest")
    public GuestDTO createGuest(@RequestBody GuestDTO guestDTO) {
        return guestRestController.createGuest(guestDTO);
    }

    @GetMapping("/refresh-token")
    public void redirectToUserRestController(HttpServletRequest request, HttpServletResponse response) throws IOException {
        userRestController.generateNewAccessToken(request, response);
    }

    @PostMapping("/login")
    public JWTTokensDTO login(@RequestBody CredentialsDTO credentials) {
        return authService.authenticate(credentials.getUsername() != null ? credentials.getUsername() : credentials.getEmail(), credentials.getPassword());
    }
}
