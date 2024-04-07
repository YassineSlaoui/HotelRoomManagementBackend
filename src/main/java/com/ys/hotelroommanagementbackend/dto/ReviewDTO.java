package com.ys.hotelroommanagementbackend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {

    private Integer rating;

    private String comment;
}
