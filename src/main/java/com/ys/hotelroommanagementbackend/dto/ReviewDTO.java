package com.ys.hotelroommanagementbackend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {

    private Long reviewId;

    private Integer rating;

    private String comment;
}
