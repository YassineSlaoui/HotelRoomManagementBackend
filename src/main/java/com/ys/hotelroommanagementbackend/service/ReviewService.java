package com.ys.hotelroommanagementbackend.service;

import com.ys.hotelroommanagementbackend.dto.ReviewDTO;
import com.ys.hotelroommanagementbackend.entity.Review;
import org.springframework.data.domain.Page;

public interface ReviewService {

    Review getReviewById(Long reviewId);

    Page<ReviewDTO> getAllReviews(int page, int size);

    Page<ReviewDTO> getRoomReviews(Long roomId, int page, int size);

    Page<ReviewDTO> getGuestReviews(Long guestId, int page, int size);

    ReviewDTO createReview(ReviewDTO review);

    ReviewDTO updateReview(ReviewDTO review);

    void deleteReview(Long reviewId);
}
