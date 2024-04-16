package com.ys.hotelroommanagementbackend.web;

import com.ys.hotelroommanagementbackend.dto.FilterDTO;
import com.ys.hotelroommanagementbackend.dto.RoomDTO;
import com.ys.hotelroommanagementbackend.mapper.RoomMapper;
import com.ys.hotelroommanagementbackend.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
@CrossOrigin("*")
@SecurityRequirement(name = "Access Token Authorization")
public class RoomRestController {
    private final RoomService roomService;
    private final RoomMapper roomMapper;

    public RoomRestController(RoomService roomService, RoomMapper roomMapper) {
        this.roomService = roomService;
        this.roomMapper = roomMapper;
    }

    @Operation(summary = "Get room by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Room found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/{roomId}")
    @PreAuthorize("hasAuthority('Admin')")
    public RoomDTO getRoomById(@PathVariable Long roomId) {
        return roomMapper.fromRoom(roomService.getRoomById(roomId));
    }

    @Operation(summary = "Get all rooms")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rooms found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping
    @PreAuthorize("permitAll()")
    public Page<RoomDTO> getAllRooms(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        return roomService.getAllRooms(page, size);
    }

    @Operation(summary = "Get rooms by filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rooms found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping("/filteredBy")
    @PreAuthorize("permitAll()")
    public Page<RoomDTO> getRoomsByFilters(@RequestBody List<FilterDTO> filters,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        return roomService.getRoomsByFilters(filters, page, size);
    }

    @Operation(summary = "Get new rooms for guest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rooms found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/guest/{guestId}/newRoomsSuggestions")
    @PreAuthorize("hasAuthority('Admin') or @securityUtil.isGuestOwner(#guestId)")
    public Page<RoomDTO> getNewRoomsForGuest(@PathVariable Long guestId,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        return roomService.getNewRoomsForGuest(guestId, page, size);
    }

    @Operation(summary = "Get previously booked rooms for guest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rooms found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/guest/{guestId}/previouslyBooked")
    @PreAuthorize("hasAuthority('Admin') or @securityUtil.isGuestOwner(#guestId)")
    public Page<RoomDTO> getPreviouslyBookedRoomsForGuest(@PathVariable Long guestId,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        return roomService.getPreviouslyBookedRoomsForGuest(guestId, page, size);
    }

    @Operation(summary = "Create a room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Room created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('Admin')")
    public RoomDTO createRoom(@RequestBody RoomDTO roomDTO) {
        return roomService.createRoom(roomDTO);
    }

    @Operation(summary = "Update a room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Room updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PutMapping("/{roomId}")
    @PreAuthorize("hasAuthority('Admin')")
    public RoomDTO updateRoom(@PathVariable Long roomId,
                              @RequestBody RoomDTO roomDTO) {
        roomDTO.setRoomId(roomId);
        return roomService.updateRoom(roomDTO);
    }

    @Operation(summary = "Delete a room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Room deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @DeleteMapping("/{roomId}")
    @PreAuthorize("hasAuthority('Admin')")
    public void deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
    }
}
