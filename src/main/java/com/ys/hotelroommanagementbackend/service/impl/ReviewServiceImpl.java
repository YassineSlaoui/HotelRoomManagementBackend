package com.ys.hotelroommanagementbackend.service.impl;

import com.ys.hotelroommanagementbackend.dao.ReviewDao;
import com.ys.hotelroommanagementbackend.dto.ReviewDTO;
import com.ys.hotelroommanagementbackend.entity.Review;
import com.ys.hotelroommanagementbackend.exception.NotFoundException;
import com.ys.hotelroommanagementbackend.mapper.ReviewMapper;
import com.ys.hotelroommanagementbackend.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewDao reviewDao;
    private final ReviewMapper reviewMapper;

    public ReviewServiceImpl(ReviewDao reviewDao, ReviewMapper reviewMapper) {
        this.reviewDao = reviewDao;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public Review getReviewById(Long reviewId) {
        return reviewDao.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review with id: " + reviewId + " not found"));
    }

    @Override
    public Page<ReviewDTO> getAllReviews(int page, int size) {
        return reviewDao.findAll(PageRequest.of(page, size)).map(reviewMapper::fromReview);
    }

    @Override
    public List<Review> getAllReviews() {
        return reviewDao.findAll();
    }

    @Override
    public Page<ReviewDTO> getRoomReviews(Long roomId, int page, int size) {
        return reviewDao.findReviewsByRoom(roomId, PageRequest.of(page, size)).map(reviewMapper::fromReview);
    }

    @Override
    public Page<ReviewDTO> getGuestReviews(Long guestId, int page, int size) {
        return reviewDao.findReviewsByGuest(guestId, PageRequest.of(page, size)).map(reviewMapper::fromReview);
    }

    @Override
    public ReviewDTO createReview(ReviewDTO review) {
        return reviewMapper.fromReview(reviewDao.save(reviewMapper.toReview(review)));
    }

    @Override
    public ReviewDTO updateReview(ReviewDTO review) {
        return reviewMapper.fromReview(reviewDao.save(reviewMapper.toReview(review)));
    }

    @Override
    public void deleteReview(Long reviewId) {
        reviewDao.deleteById(reviewId);
    }
}
