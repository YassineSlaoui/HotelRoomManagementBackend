package com.ys.hotelroommanagementbackend.service;

import com.ys.hotelroommanagementbackend.entity.Role;

@SuppressWarnings("UnusedReturnValue")
public interface RoleService {

    Role createRole(String roleName);

    Role getRole(String roleName);
}
