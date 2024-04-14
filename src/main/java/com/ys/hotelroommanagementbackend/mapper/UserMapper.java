package com.ys.hotelroommanagementbackend.mapper;

import com.ys.hotelroommanagementbackend.dto.UserDTO;
import com.ys.hotelroommanagementbackend.entity.Role;
import com.ys.hotelroommanagementbackend.entity.User;
import com.ys.hotelroommanagementbackend.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserMapper {

    final RoleService roleService;

    public UserMapper(RoleService roleService) {
        this.roleService = roleService;
    }

    public UserDTO fromUser(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        return userDTO;
    }

    public User toUser(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return user;
    }
}
