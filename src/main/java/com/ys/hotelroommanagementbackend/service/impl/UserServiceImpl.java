package com.ys.hotelroommanagementbackend.service.impl;

import com.ys.hotelroommanagementbackend.dao.UserDao;
import com.ys.hotelroommanagementbackend.entity.Role;
import com.ys.hotelroommanagementbackend.entity.User;
import com.ys.hotelroommanagementbackend.service.RoleService;
import com.ys.hotelroommanagementbackend.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    private RoleService roleService;

    public UserServiceImpl(UserDao userDao, RoleService roleService) {
        this.userDao = userDao;
        this.roleService = roleService;
    }

    @Override
    public User getUserByEmail(String email) {
        return userDao.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email " + email + " not found"));
    }

    @Override
    public User getUserByUsername(String username) {
        return userDao.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User with username " + username + " not found"));
    }

    @Override
    public User createUser(String username, String email, String password) {
        return userDao.save(User.builder().email(email).password(password).username(username).build());
    }

    @Override
    public User createUserWithEmail(String email, String password) {
        return userDao.save(User.builder().email(email).password(password).build());
    }

    @Override
    public User createUserWithUsername(String username, String password) {
        return userDao.save(User.builder().username(username).password(password).build());
    }

    @Override
    public User updateUser(User user) {
        return userDao.save(user);
    }

    @Override
    public void assignRoleToUser(String email, String roleName) {
        User user = getUserByEmail(email);
        Role role = roleService.getRole(roleName);
        user.assignRole(role);
    }

    @Override
    public void revokeRoleFromUser(String email, String roleName) {
        User user = getUserByEmail(email);
        Role role = roleService.getRole(roleName);
        user.revokeRole(role);
    }

    @Override
    public void deleteUser(Long userId) {
        userDao.deleteById(userId);
    }
}
