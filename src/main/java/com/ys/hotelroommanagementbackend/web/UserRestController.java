package com.ys.hotelroommanagementbackend.web;

import com.ys.hotelroommanagementbackend.dto.UserDTO;
import com.ys.hotelroommanagementbackend.mapper.UserMapper;
import com.ys.hotelroommanagementbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin("*")
@SecurityRequirement(name = "Access Token Authorization")
public class UserRestController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserRestController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error"),
            @ApiResponse(responseCode = "403", description = "Access to this resource is forbidden",
                    content = @Content(schema = @Schema(type = "object", implementation = Map.class,
                            properties = {
                                    @StringToClassMapItem(key = "timestamp", value = Date.class),
                                    @StringToClassMapItem(key = "status", value = Integer.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                            })))
    })
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
            @ApiResponse(responseCode = "500", description = "Server error"),
            @ApiResponse(responseCode = "403", description = "Access to this resource is forbidden",
                    content = @Content(schema = @Schema(type = "object", implementation = Map.class,
                            properties = {
                                    @StringToClassMapItem(key = "timestamp", value = Date.class),
                                    @StringToClassMapItem(key = "status", value = Integer.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                            }))),
            @ApiResponse(responseCode = "404", description = "Requested resource not found")
    })
    @GetMapping("/{username_email_or_id}")
    @PreAuthorize("hasAuthority('Admin') or @securityUtil.isUserOwner(#username_email_or_id)")
    public UserDTO getUserByAny(@PathVariable String username_email_or_id) {
        return userMapper.fromUser(userService.getUserByUsernameOrEmailOrId(username_email_or_id));
    }

    @Operation(summary = "Get user by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error"),
            @ApiResponse(responseCode = "403", description = "Access to this resource is forbidden",
                    content = @Content(schema = @Schema(type = "object", implementation = Map.class,
                            properties = {
                                    @StringToClassMapItem(key = "timestamp", value = Date.class),
                                    @StringToClassMapItem(key = "status", value = Integer.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                            }))),
            @ApiResponse(responseCode = "404", description = "Requested resource not found")
    })
    @GetMapping("/byEmail/{email}")
    @PreAuthorize("hasAuthority('Admin') or @securityUtil.isUserOwner(#email)")
    public UserDTO getUserByEmail(@PathVariable String email) {
        return userMapper.fromUser(userService.getUserByEmail(email));
    }

    @Operation(summary = "Get user by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error"),
            @ApiResponse(responseCode = "403", description = "Access to this resource is forbidden",
                    content = @Content(schema = @Schema(type = "object", implementation = Map.class,
                            properties = {
                                    @StringToClassMapItem(key = "timestamp", value = Date.class),
                                    @StringToClassMapItem(key = "status", value = Integer.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                            }))),
            @ApiResponse(responseCode = "404", description = "Requested resource not found")
    })
    @GetMapping("/byUsername/{username}")
    @PreAuthorize("hasAuthority('Admin') or @securityUtil.isUserOwner(#username)")
    public UserDTO getUserByUsername(@PathVariable String username) {
        return userMapper.fromUser(userService.getUserByUsername(username));
    }

    @Operation(summary = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error"),
            @ApiResponse(responseCode = "403", description = "Access to this resource is forbidden",
                    content = @Content(schema = @Schema(type = "object", implementation = Map.class,
                            properties = {
                                    @StringToClassMapItem(key = "timestamp", value = Date.class),
                                    @StringToClassMapItem(key = "status", value = Integer.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                            }))),
            @ApiResponse(responseCode = "404", description = "Requested resource not found")
    })
    @GetMapping("/byId/{userId}")
    @PreAuthorize("hasAuthority('Admin') or @securityUtil.isUserOwner(#userId)")
    public UserDTO getUserById(@PathVariable long userId) {
        return userMapper.fromUser(userService.getUserById(userId));
    }

    @Operation(summary = "Create a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error"),
            @ApiResponse(responseCode = "403", description = "Access to this resource is forbidden",
                    content = @Content(schema = @Schema(type = "object", implementation = Map.class,
                            properties = {
                                    @StringToClassMapItem(key = "timestamp", value = Date.class),
                                    @StringToClassMapItem(key = "status", value = Integer.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                            })))
    })
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
            @ApiResponse(responseCode = "500", description = "Server error"),
            @ApiResponse(responseCode = "403", description = "Access to this resource is forbidden",
                    content = @Content(schema = @Schema(type = "object", implementation = Map.class,
                            properties = {
                                    @StringToClassMapItem(key = "timestamp", value = Date.class),
                                    @StringToClassMapItem(key = "status", value = Integer.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                            })))
    })
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
            @ApiResponse(responseCode = "500", description = "Server error"),
            @ApiResponse(responseCode = "403", description = "Access to this resource is forbidden",
                    content = @Content(schema = @Schema(type = "object", implementation = Map.class,
                            properties = {
                                    @StringToClassMapItem(key = "timestamp", value = Date.class),
                                    @StringToClassMapItem(key = "status", value = Integer.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                            })))
    })
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
            @ApiResponse(responseCode = "500", description = "Server error"),
            @ApiResponse(responseCode = "403", description = "Access to this resource is forbidden",
                    content = @Content(schema = @Schema(type = "object", implementation = Map.class,
                            properties = {
                                    @StringToClassMapItem(key = "timestamp", value = Date.class),
                                    @StringToClassMapItem(key = "status", value = Integer.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                            }))),
            @ApiResponse(responseCode = "404", description = "Requested resource not found")
    })
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
            @ApiResponse(responseCode = "500", description = "Server error"),
            @ApiResponse(responseCode = "403", description = "Access to this resource is forbidden",
                    content = @Content(schema = @Schema(type = "object", implementation = Map.class,
                            properties = {
                                    @StringToClassMapItem(key = "timestamp", value = Date.class),
                                    @StringToClassMapItem(key = "status", value = Integer.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                            }))),
            @ApiResponse(responseCode = "404", description = "Requested resource not found")
    })
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
            @ApiResponse(responseCode = "500", description = "Server error"),
            @ApiResponse(responseCode = "403", description = "Access to this resource is forbidden",
                    content = @Content(schema = @Schema(type = "object", implementation = Map.class,
                            properties = {
                                    @StringToClassMapItem(key = "timestamp", value = Date.class),
                                    @StringToClassMapItem(key = "status", value = Integer.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                            }))),
            @ApiResponse(responseCode = "404", description = "Requested resource not found")
    })
    @PostMapping("/revokeRole")
    @PreAuthorize("hasAuthority('Admin')")
    public void revokeRoleFromUser(@RequestParam String usernameOrEmail,
                                   @RequestParam String roleName) {
        userService.revokeRoleFromUser(usernameOrEmail, roleName);
    }

    @Operation(summary = "Delete a user", description = "If the user is not an admin, the user will be logged out")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error"),
            @ApiResponse(responseCode = "403", description = "Access to this resource is forbidden",
                    content = @Content(schema = @Schema(type = "object", implementation = Map.class,
                            properties = {
                                    @StringToClassMapItem(key = "timestamp", value = Date.class),
                                    @StringToClassMapItem(key = "status", value = Integer.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                            }))),
            @ApiResponse(responseCode = "404", description = "Requested resource not found")
    })
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
