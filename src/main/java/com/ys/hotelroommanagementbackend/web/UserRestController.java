package com.ys.hotelroommanagementbackend.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.hotelroommanagementbackend.dto.UserDTO;
import com.ys.hotelroommanagementbackend.entity.Role;
import com.ys.hotelroommanagementbackend.entity.User;
import com.ys.hotelroommanagementbackend.helper.JWTHelper;
import com.ys.hotelroommanagementbackend.mapper.UserMapper;
import com.ys.hotelroommanagementbackend.security.TokenValidationService;
import com.ys.hotelroommanagementbackend.service.UserService;
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

import java.io.IOException;
import java.util.stream.Collectors;

import static com.ys.hotelroommanagementbackend.constant.JWTUtil.*;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin("*")
public class UserRestController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final JWTHelper jwtHelper;
    private final TokenValidationService invalidatedTokenService;

    public UserRestController(UserService userService, UserMapper userMapper, JWTHelper jwtHelper, TokenValidationService invalidatedTokenService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.jwtHelper = jwtHelper;
        this.invalidatedTokenService = invalidatedTokenService;
    }

    @Operation(summary = "Generate new access token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New access token generated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @SecurityRequirement(name = "Refresh Token Authorization")
    @GetMapping("/refresh-token")
    public void generateNewAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String jwtRefreshToken = jwtHelper.extractTokenFromHeaderIfExists(request.getHeader(AUTH_HEADER));
        if (!invalidatedTokenService.isTokenValid(jwtRefreshToken))
            throw new RuntimeException("Refresh token is invalid");
        if (jwtRefreshToken != null) {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(jwtRefreshToken);
            String usernameOrEmail = decodedJWT.getSubject();
            User user = userService.getUserByUsernameOrEmail(usernameOrEmail);
            invalidatedTokenService.invalidateUserTokens(user);
            String jwtAccessToken = jwtHelper.generateAccessToken(usernameOrEmail, user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
            String newJwtRefreshToken = jwtHelper.generateRefreshToken(user.getUsername());
            response.setContentType("application/json");
            new ObjectMapper().writeValue(response.getOutputStream(), jwtHelper.getTokensMap(jwtAccessToken, newJwtRefreshToken));
        } else
            throw new RuntimeException("Refresh token required");
    }

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @SecurityRequirement(name = "Access Token Authorization")
    @GetMapping
    @PreAuthorize("hasAuthority('Admin')")
    public Page<UserDTO> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        return userService.getAllUsers(page, size);
    }

    @Operation(summary = "Get user by any identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @SecurityRequirement(name = "Access Token Authorization")
    @GetMapping("/{username_email_or_id}")
    @PreAuthorize("hasAuthority('Admin') or @securityUtil.isUserOwner(#username_email_or_id)")
    public UserDTO getUserByAny(@PathVariable String username_email_or_id) {
        return userMapper.fromUser(userService.getUserByUsernameOrEmailOrId(username_email_or_id));
    }

    @Operation(summary = "Get user by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @SecurityRequirement(name = "Access Token Authorization")
    @GetMapping("/byEmail/{email}")
    @PreAuthorize("hasAuthority('Admin') or @securityUtil.isUserOwner(#email)")
    public UserDTO getUserByEmail(@PathVariable String email) {
        return userMapper.fromUser(userService.getUserByEmail(email));
    }

    @Operation(summary = "Get user by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @SecurityRequirement(name = "Access Token Authorization")
    @GetMapping("/byUsername/{username}")
    @PreAuthorize("hasAuthority('Admin') or @securityUtil.isUserOwner(#username)")
    public UserDTO getUserByUsername(@PathVariable String username) {
        return userMapper.fromUser(userService.getUserByUsername(username));
    }

    @Operation(summary = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @SecurityRequirement(name = "Access Token Authorization")
    @GetMapping("/byId/{userId}")
    @PreAuthorize("hasAuthority('Admin') or @securityUtil.isUserOwner(#userId)")
    public UserDTO getUserById(@PathVariable long userId) {
        return userMapper.fromUser(userService.getUserById(userId));
    }

    @Operation(summary = "Create a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @SecurityRequirement(name = "Access Token Authorization")
    @PostMapping
    @PreAuthorize("hasAuthority('Admin')")
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        UserDTO saved = userMapper.fromUser(userService.createUser(userDTO.getUsername(), userDTO.getEmail(), userDTO.getPassword()));
        if (userDTO.getRoles() != null)
            userDTO.getRoles().forEach(role -> userService
                    .assignRoleToUser(userDTO.getUsername().isBlank() ? userDTO.getEmail() : userDTO.getUsername(), role));
        return saved;
    }

    @Operation(summary = "Create a user with email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @SecurityRequirement(name = "Access Token Authorization")
    @PostMapping("/createWithEmail")
    @PreAuthorize("hasAuthority('Admin')")
    public UserDTO createUserWithEmail(@RequestParam String email,
                                       @RequestParam String password) {
        return userMapper.fromUser(userService.createUserWithEmail(email, password));
    }

    @Operation(summary = "Create a user with username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @SecurityRequirement(name = "Access Token Authorization")
    @PostMapping("/createWithUsername")
    @PreAuthorize("hasAuthority('Admin')")
    public UserDTO createUserWithUsername(@RequestParam String username,
                                          @RequestParam String password) {
        return userMapper.fromUser(userService.createUserWithUsername(username, password));
    }

    @Operation(summary = "Update a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @SecurityRequirement(name = "Access Token Authorization")
    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('Admin') or @securityUtil.isUserOwner(#userId)")
    public UserDTO updateUser(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
        userDTO.setUserId(userId);
        return userMapper.fromUser(userService.updateUser(userMapper.toUser(userDTO)));
    }

    @Operation(summary = "Assign role to user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role assigned"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @SecurityRequirement(name = "Access Token Authorization")
    @PostMapping("/assignRole")
    @PreAuthorize("hasAuthority('Admin')")
    public void assignRoleToUser(@RequestParam String usernameOrEmail,
                                 @RequestParam String roleName) {
        userService.assignRoleToUser(usernameOrEmail, roleName);
    }

    @Operation(summary = "Revoke role from user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role revoked"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @SecurityRequirement(name = "Access Token Authorization")
    @PostMapping("/revokeRole")
    @PreAuthorize("hasAuthority('Admin')")
    public void revokeRoleFromUser(@RequestParam String usernameOrEmail,
                                   @RequestParam String roleName) {
        userService.revokeRoleFromUser(usernameOrEmail, roleName);
    }

    @Operation(summary = "Delete a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @SecurityRequirement(name = "Access Token Authorization")
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('Admin') or @securityUtil.isUserOwner(#userId)")
    public void deleteUser(@PathVariable Long userId, HttpServletRequest request, HttpServletResponse response) {
        userService.deleteUser(userId);

        if (!SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .toList().contains("Admin")) {
            SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
            logoutHandler.logout(request, response, null);
        }
    }
}
