package com.ys.hotelroommanagementbackend.dto;

import lombok.*;

import java.util.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RoomDTO {

    private Long roomId;

    private String roomNumber;

    private String roomType;

    private String description;

    private Double price;

    private Integer capacity;

    private Boolean available;

    private Boolean maintenance;

    @Builder.Default
    private List<String> photoURLs = new ArrayList<>();
}
