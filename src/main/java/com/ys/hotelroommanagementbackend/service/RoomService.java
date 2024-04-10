package com.ys.hotelroommanagementbackend.service;

import com.ys.hotelroommanagementbackend.dto.FilterDTO;
import com.ys.hotelroommanagementbackend.dto.RoomDTO;
import com.ys.hotelroommanagementbackend.entity.Room;
import org.springframework.data.domain.Page;

import java.util.List;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public interface RoomService {

    Room getRoomById(Long roomId);

    Page<RoomDTO> getAllRooms(int page, int size);

    Page<RoomDTO> getRoomsByFilters(List<FilterDTO> filters, int page, int size);

    Page<RoomDTO> getNewRoomsForGuest(Long guestId, int page, int size);

    Page<RoomDTO> getPreviouslyBookedRoomsForGuest(Long guestId, int page, int size);

    List<Room> getAllRooms();

    RoomDTO createRoom(RoomDTO roomDTO);

    RoomDTO updateRoom(RoomDTO roomDTO);

    void deleteRoom(Long roomId);
}
