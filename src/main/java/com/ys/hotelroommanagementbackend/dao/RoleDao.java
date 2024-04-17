package com.ys.hotelroommanagementbackend.dao;

import com.ys.hotelroommanagementbackend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface RoleDao extends JpaRepository<Role, Long> {

    Optional<Role> findRoleByName(String name);

    Optional<Role> findRoleByNameIgnoreCase(String name);
}
