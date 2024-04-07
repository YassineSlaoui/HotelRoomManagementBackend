package com.ys.hotelroommanagementbackend.dao;

import com.ys.hotelroommanagementbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDao extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);

    List<User> findUsersByUsernameContainsIgnoreCaseOrEmailContainsIgnoreCase(String username, String email);

    List<User> findUsersByUsernameContainsIgnoreCase(String username);

    Optional<User> findUserByEmailAndPassword(String email, String password);

    Optional<User> findUserByUsernameAndPassword(String email, String password);
}
