package com.ys.hotelroommanagementbackend.service.impl;

import com.ys.hotelroommanagementbackend.dao.RoleDao;
import com.ys.hotelroommanagementbackend.entity.Role;
import com.ys.hotelroommanagementbackend.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public Role createRole(String roleName) {
        return roleDao.save(Role.builder().name(roleName).build());
    }

    @Override
    public Role getRole(String roleName) {
        return roleDao.findRoleByNameIgnoreCase(roleName).orElseThrow(() -> new RuntimeException("Role " + roleName + " not found"));
    }
}
