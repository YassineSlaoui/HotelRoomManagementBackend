package com.ys.hotelroommanagementbackend.web;

import com.ys.hotelroommanagementbackend.dto.FilterDTO;
import com.ys.hotelroommanagementbackend.dto.RoomDTO;
import com.ys.hotelroommanagementbackend.mapper.RoomMapper;
import com.ys.hotelroommanagementbackend.service.RoomService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/rooms")
public class RoomRestController {
    private final RoomService roomService;
    private final RoomMapper roomMapper;

    public RoomRestController(RoomService roomService, RoomMapper roomMapper) {
        this.roomService = roomService;
        this.roomMapper = roomMapper;
    }

    @GetMapping("/{roomId}")
    public RoomDTO getRoomById(@PathVariable Long roomId) {
        return roomMapper.fromRoom(roomService.getRoomById(roomId));
    }

    @GetMapping
    public Page<RoomDTO> getAllRooms(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        return roomService.getAllRooms(page, size);
    }

    @PostMapping("/filteredBy")
    public Page<RoomDTO> getRoomsByFilters(@RequestBody List<FilterDTO> filters,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        return roomService.getRoomsByFilters(filters, page, size);
    }

    @GetMapping("/guest/{guestId}/newRoomsSuggestions")
    public Page<RoomDTO> getNewRoomsForGuest(@PathVariable Long guestId,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        return roomService.getNewRoomsForGuest(guestId, page, size);
    }

    @GetMapping("/guest/{guestId}/previouslyBooked")
    public Page<RoomDTO> getPreviouslyBookedRoomsForGuest(@PathVariable Long guestId,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        return roomService.getPreviouslyBookedRoomsForGuest(guestId, page, size);
    }

    @PostMapping
    public RoomDTO createRoom(@RequestBody RoomDTO roomDTO) {
        return roomService.createRoom(roomDTO);
    }

    @PutMapping("/{roomId}")
    public RoomDTO updateRoom(@PathVariable Long roomId,
                              @RequestBody RoomDTO roomDTO) {
        roomDTO.setRoomId(roomId);
        return roomService.updateRoom(roomDTO);
    }

    @DeleteMapping("/{roomId}")
    public void deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
    }
}
