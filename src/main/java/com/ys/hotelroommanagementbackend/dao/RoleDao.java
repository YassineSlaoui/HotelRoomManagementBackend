package com.ys.hotelroommanagementbackend.dao;

import com.ys.hotelroommanagementbackend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDao extends JpaRepository<Role, Long> {
    Role findRoleByName(String name);
}
