package com.ys.hotelroommanagementbackend.runner;

import com.ys.hotelroommanagementbackend.dao.*;
import com.ys.hotelroommanagementbackend.entity.Guest;
import com.ys.hotelroommanagementbackend.entity.Role;
import com.ys.hotelroommanagementbackend.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CLIRunner implements CommandLineRunner, InitializingBean {

    @Autowired
    RoleDao roleDao;

    @Autowired
    UserDao userDao;

    @Autowired
    GuestDao guestDao;

    @Autowired
    RoomDao roomDao;

    @Autowired
    ReservationDao reservationDao;

    @Autowired
    ReviewDao reviewDao;

    @Override
    public void run(String... args) throws Exception {
        createRoles();
        updateRoles();
        readRoles();
        createUsers();
        updateUser();
        readUser();
        User janeUser = User.builder().username("jane").email("jane@domain.com").password("jane").build();
        janeUser = userDao.save(janeUser);
        janeUser.assignRole(roleDao.findAll().getFirst());
        userDao.save(janeUser);
        guestDao.save(Guest.builder().firstName("Jane").lastName("Doe").contactInfo("None").user(janeUser).build());
//        guestDao.deleteById(guestDao.findGuestByUsername("jane").get().getGuestId());
        userDao.findAll(PageRequest.of(0, 2)).forEach(System.out::println);
    }


    private void createRoles() {
        roleDao.saveAll(Arrays.asList(
                Role.builder().name("admin").build(),
                Role.builder().name("guest").build()
        ));
    }

    private void readRoles() {
        roleDao.findAll().forEach(System.out::println);
    }

    /**
     * This will make the role names titles
     *
     * @author Yassine Slaoui
     */
    private void updateRoles() {
        roleDao.findAll().forEach(role -> {
            role.setName(role.getName().substring(0, 1).toUpperCase() + role.getName().substring(1).toLowerCase());
            roleDao.save(role);
        });
    }

    private void createUsers() {
        userDao.saveAll(Arrays.asList(
                User.builder().username("user1").email("user1@domain.com").password("pass1").build(),
                User.builder().username("user2").email("user2@domain.com").password("pass2").build(),
                User.builder().username("user3").email("user3@domain.com").password("pass3").build(),
                User.builder().username("user4").email("user4@domain.com").password("pass4").build()));
        userDao.findAll().forEach(user -> {
            user.assignRole(roleDao.findAll().getFirst());
            userDao.save(user);
        });
    }

    private void readUser() {
        userDao.findUserByEmail("user1@domain.com").ifPresentOrElse(user -> {
            System.out.println(user);
            user.getRoles().forEach(role -> System.out.println("Role: " + role.getName()));
        }, () -> System.err.println("User Not Found"));
        userDao.findUserByEmail("user3@domain.com").ifPresentOrElse(System.out::println, () -> System.err.println("User Not Found"));
        userDao.findUserByEmail("user3@domain.com").ifPresentOrElse(System.out::println, () -> System.err.println("User Not Found"));
    }


    private void updateUser() {
        User user = userDao.findById(2L).orElseThrow(() -> new EntityNotFoundException("User with id 2 not found"));
        user.setEmail("user2new@domain.com");
        userDao.save(user);

    }

    private void deleteUser() {
        userDao.deleteById(3L);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
//        jdbcTemplate.execute("CREATE EXTENSION IF NOT EXISTS citext;");
    }
}
