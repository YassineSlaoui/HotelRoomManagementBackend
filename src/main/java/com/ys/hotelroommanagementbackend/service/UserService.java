package com.ys.hotelroommanagementbackend.service;

import com.ys.hotelroommanagementbackend.dto.UserDTO;
import com.ys.hotelroommanagementbackend.entity.User;
import org.springframework.data.domain.Page;

@SuppressWarnings("unused")
public interface UserService {

    Page<UserDTO> getAllUsers(int page, int size);

    User getUserById(Long userId);

    User getUserByEmail(String email);

    User getUserByUsername(String username);

    User getUserByUsernameOrEmail(String usernameOrEmail);

    User getUserByUsernameOrEmailOrId(String keyword);

    User createUser(String username, String email, String password);

    User createUserWithEmail(String email, String password);

    User createUserWithUsername(String username, String password);

    User updateUser(User user);

    void assignRoleToUser(String email, String roleName);

    void revokeRoleFromUser(String email, String roleName);

    void deleteUser(Long userId);
}
