package com.ys.hotelroommanagementbackend.dao;

import com.ys.hotelroommanagementbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDao extends JpaRepository<User, Long> {
    User findUserByEmail(String email);

    List<User> findUsersByUsernameContainsIgnoreCaseOrEmailContainsIgnoreCase(String username, String email);

    List<User> findUsersByUsernameContainsIgnoreCase(String username);

    User findUserByEmailAndPassword(String email, String password);

    User findUserByUsernameAndPassword(String email, String password);
}
