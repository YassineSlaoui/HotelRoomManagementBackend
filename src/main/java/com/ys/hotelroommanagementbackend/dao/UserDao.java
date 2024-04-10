package com.ys.hotelroommanagementbackend.dao;

import com.ys.hotelroommanagementbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface UserDao extends JpaRepository<User, Long> {

    List<User> findUsersByUsernameContainsIgnoreCaseOrEmailContainsIgnoreCase(String username, String email);

    List<User> findUsersByUsernameContainsIgnoreCase(String username);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByUsername(String username);
}
