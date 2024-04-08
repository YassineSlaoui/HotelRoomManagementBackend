package com.ys.hotelroommanagementbackend.service;

import com.ys.hotelroommanagementbackend.entity.User;

public interface UserService {

    User loadUserByEmail(String email);

    User loadUserByUsername(String username);

    User createUser(String username, String email, String password);

    User createUserWithEmail(String email, String password);

    User createUserWithUsername(String username, String password);

    User updateUser(User user);

    void assignRoleToUser(String email, String roleName);

    void revokeRoleFromUser(String email, String roleName);

    void deleteUser(Long userId);
}
