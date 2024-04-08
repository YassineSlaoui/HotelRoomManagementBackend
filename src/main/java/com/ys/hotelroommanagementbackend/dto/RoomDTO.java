package com.ys.hotelroommanagementbackend.dto;

import lombok.*;

import java.util.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDTO {

    @Setter(AccessLevel.NONE)
    private Long roomId;

    private String roomNumber;

    private String roomType;

    private String description;

    private Double price;

    private Integer capacity;

    private Boolean available;

    @Builder.Default
    private List<String> photoURLs = new ArrayList<>();

    private Date availabilityStartDate;

    private Date availabilityEndDate;
}
