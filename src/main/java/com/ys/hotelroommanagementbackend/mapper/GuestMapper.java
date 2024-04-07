package com.ys.hotelroommanagementbackend.mapper;

import com.ys.hotelroommanagementbackend.dto.GuestDTO;
import com.ys.hotelroommanagementbackend.entity.Guest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class GuestMapper {

    UserMapper userMapper;

    public GuestMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public GuestDTO fromGuest(Guest guest) {
        GuestDTO guestDTO = new GuestDTO();
        BeanUtils.copyProperties(guest, guestDTO);
        guestDTO.setUser(userMapper.fromUser(guest.getUser()));
        return guestDTO;
    }

    public Guest toGuest(GuestDTO guestDTO) {
        Guest guest = new Guest();
        BeanUtils.copyProperties(guestDTO, guest);
        return guest;
    }
}
