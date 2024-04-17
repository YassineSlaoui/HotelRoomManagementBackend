package com.ys.hotelroommanagementbackend.web;

import com.ys.hotelroommanagementbackend.dto.CredentialsDTO;
import com.ys.hotelroommanagementbackend.dto.GuestDTO;
import com.ys.hotelroommanagementbackend.dto.JWTTokensDTO;
import com.ys.hotelroommanagementbackend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/noauth")
@CrossOrigin("*")
public class NoAuthRestController {

    private final GuestRestController guestRestController;
    private final UserRestController userRestController;
    private final AuthService authService;


    public NoAuthRestController(GuestRestController guestRestController, UserRestController userRestController, AuthService authService) {
        this.guestRestController = guestRestController;
        this.userRestController = userRestController;
        this.authService = authService;
    }

    @Operation(summary = "Create a new guest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Guest created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping("/signup/guest")
    public GuestDTO createGuest(@RequestBody GuestDTO guestDTO) {
        return guestRestController.createGuest(guestDTO);
    }

    @SecurityRequirement(name = "Refresh Token Authorization")
    @Operation(summary = "Refresh access token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Access token refreshed"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/refresh-token")
    public JWTTokensDTO redirectToUserRestController(HttpServletRequest request) throws IOException {
        return authService.handleRefreshToken(request);
    }

    @Operation(summary = "Authenticate a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping("/login")
    public JWTTokensDTO login(@RequestBody CredentialsDTO credentials) {
        return authService.authenticate(credentials.getUsername() != null ? credentials.getUsername() : credentials.getEmail(), credentials.getPassword());
    }
}
