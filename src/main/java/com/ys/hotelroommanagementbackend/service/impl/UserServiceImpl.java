package com.ys.hotelroommanagementbackend.service.impl;

import com.ys.hotelroommanagementbackend.dao.UserDao;
import com.ys.hotelroommanagementbackend.dto.UserDTO;
import com.ys.hotelroommanagementbackend.entity.Role;
import com.ys.hotelroommanagementbackend.entity.User;
import com.ys.hotelroommanagementbackend.mapper.UserMapper;
import com.ys.hotelroommanagementbackend.service.RoleService;
import com.ys.hotelroommanagementbackend.service.UserService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserDao userDao, RoleService roleService, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userDao = userDao;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public Page<UserDTO> getAllUsers(int page, int size) {
        return userDao.findAll(PageRequest.of(page, size)).map(userMapper::fromUser);
    }

    @Override
    public User getUserById(Long userId) {
        return userDao.findById(userId).orElseThrow(() -> new RuntimeException("User with id " + userId + " not found"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userDao.findUserByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User with email " + email + " not found"));
    }

    @Override
    public User getUserByUsername(String username) {
        return userDao.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new RuntimeException("User with username " + username + " not found"));
    }

    @Override
    public User getUserByUsernameOrEmail(String usernameOrEmail) {
        if (EmailValidator.getInstance().isValid(usernameOrEmail))
            return getUserByEmail(usernameOrEmail);
        else
            return getUserByUsername(usernameOrEmail);
    }

    @Override
    public User getUserByUsernameOrEmailOrId(String keyword) {
        User user;

        long probableId;

        try {
            probableId = Long.parseLong(keyword);
        } catch (NumberFormatException e) {
            probableId = 0L;
        }
        if (EmailValidator.getInstance().isValid(keyword))
            return getUserByEmail(keyword);
        else {
            try {
                user = getUserByUsername(keyword);
            } catch (RuntimeException e1) {
                try {
                    user = getUserById(probableId);
                } catch (RuntimeException e2) {
                    throw new RuntimeException("Could not find User with id or username matching: " + keyword);
                }
            }
        }
        return user;
    }

    @Override
    public User createUser(String username, String email, String password) {
        if (!EmailValidator.getInstance().isValid(email))
            throw new RuntimeException("Email not Valid");
        return userDao.save(User.builder().email(email).password(passwordEncoder.encode(password)).username(username).build());
    }

    @Override
    public User createUserWithEmail(String email, String password) {
        if (!EmailValidator.getInstance().isValid(email))
            throw new RuntimeException("Email not Valid");
        return userDao.save(User.builder().email(email).password(passwordEncoder.encode(password)).build());
    }

    @Override
    public User createUserWithUsername(String username, String password) {
        return userDao.save(User.builder().username(username).password(passwordEncoder.encode(password)).build());
    }

    @Override
    public User updateUser(User user) {
        User loadedUser = userDao.findById(user.getUserId()).orElseThrow(() -> new RuntimeException("User with id: " + user.getUserId() + " Not Found"));
        // Keep roles untouched.
        loadedUser.getRoles().forEach(role -> user.getRoles().add(role));
        return userDao.save(user);
    }

    @Override
    public void assignRoleToUser(String keyword, String roleName) {
        User user = getUserByUsernameOrEmail(keyword);
        Role role = roleService.getRole(roleName);
        user.assignRole(role);
    }

    @Override
    public void revokeRoleFromUser(String keyword, String roleName) {
        User user = getUserByUsernameOrEmail(keyword);
        Role role = roleService.getRole(roleName);
        user.revokeRole(role);
    }

    @Override
    public void deleteUser(Long userId) {
        userDao.deleteById(userId);
    }
}
