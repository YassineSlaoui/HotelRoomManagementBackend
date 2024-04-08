package com.ys.hotelroommanagementbackend.service;

import com.ys.hotelroommanagementbackend.entity.Role;

public interface RoleService {

    Role createRole(String roleName);

    Role findRole(String roleName);
}
