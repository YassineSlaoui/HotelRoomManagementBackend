package com.ys.hotelroommanagementbackend.web;

import com.ys.hotelroommanagementbackend.dto.UserDTO;
import com.ys.hotelroommanagementbackend.mapper.UserMapper;
import com.ys.hotelroommanagementbackend.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api")
public class UserRestController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserRestController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/byEmail/{email}")
    public UserDTO getUserByEmail(@PathVariable String email) {
        return userMapper.fromUser(userService.getUserByEmail(email));
    }

    @GetMapping("/byUsername/{username}")
    public UserDTO getUserByUsername(@PathVariable String username) {
        return userMapper.fromUser(userService.getUserByUsername(username));
    }

    @PostMapping("/create")
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

    @PutMapping("/update")
    public UserDTO updateUser(@RequestBody UserDTO userDTO) {
        return userMapper.fromUser(userService.updateUser(userMapper.toUser(userDTO)));
    }

    @PostMapping("/assignRole")
    public void assignRoleToUser(@RequestParam String email,
                                 @RequestParam String roleName) {
        userService.assignRoleToUser(email, roleName);
    }

    @PostMapping("/revokeRole")
    public void revokeRoleFromUser(@RequestParam String email,
                                   @RequestParam String roleName) {
        userService.revokeRoleFromUser(email, roleName);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
