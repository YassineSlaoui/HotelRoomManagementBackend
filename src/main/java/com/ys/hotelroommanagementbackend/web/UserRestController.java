package com.ys.hotelroommanagementbackend.web;

import com.ys.hotelroommanagementbackend.dto.UserDTO;
import com.ys.hotelroommanagementbackend.mapper.UserMapper;
import com.ys.hotelroommanagementbackend.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/users")
public class UserRestController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserRestController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public Page<UserDTO> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        return userService.getAllUsers(page, size);
    }

    @GetMapping("/{username_email_or_id}")
    public UserDTO getUserByAny(@PathVariable String username_email_or_id) {
        return userMapper.fromUser(userService.getUserByUsernameOrEmailOrId(username_email_or_id));
    }

    @GetMapping("/byEmail/{email}")
    public UserDTO getUserByEmail(@PathVariable String email) {
        return userMapper.fromUser(userService.getUserByEmail(email));
    }

    @GetMapping("/byUsername/{username}")
    public UserDTO getUserByUsername(@PathVariable String username) {
        return userMapper.fromUser(userService.getUserByUsername(username));
    }

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        return userMapper.fromUser(userService.createUser(userDTO.getUsername(), userDTO.getEmail(), userDTO.getPassword()));
    }

    @PostMapping("/createWithEmail")
    public UserDTO createUserWithEmail(@RequestParam String email,
                                       @RequestParam String password) {
        return userMapper.fromUser(userService.createUserWithEmail(email, password));
    }

    @PostMapping("/createWithUsername")
    public UserDTO createUserWithUsername(@RequestParam String username,
                                          @RequestParam String password) {
        return userMapper.fromUser(userService.createUserWithUsername(username, password));
    }

    @PutMapping("/{userId}")
    public UserDTO updateUser(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
        userDTO.setUserId(userId);
        return userMapper.fromUser(userService.updateUser(userMapper.toUser(userDTO)));
    }

    @PostMapping("/assignRole")
    public void assignRoleToUser(@RequestParam String usernameOrEmail,
                                 @RequestParam String roleName) {
        userService.assignRoleToUser(usernameOrEmail, roleName);
    }

    @PostMapping("/revokeRole")
    public void revokeRoleFromUser(@RequestParam String usernameOrEmail,
                                   @RequestParam String roleName) {
        userService.revokeRoleFromUser(usernameOrEmail, roleName);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
