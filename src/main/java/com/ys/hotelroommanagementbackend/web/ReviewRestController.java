package com.ys.hotelroommanagementbackend.web;

import com.ys.hotelroommanagementbackend.dto.ReviewDTO;
import com.ys.hotelroommanagementbackend.mapper.ReviewMapper;
import com.ys.hotelroommanagementbackend.service.ReviewService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
@CrossOrigin("*")
@SecurityRequirement(name = "Authorization")
public class ReviewRestController {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    public ReviewRestController(ReviewService reviewService, ReviewMapper reviewMapper) {
        this.reviewService = reviewService;
        this.reviewMapper = reviewMapper;
    }

    @GetMapping("/{reviewId}")
    @PreAuthorize("permitAll()")
    public ReviewDTO getReviewById(@PathVariable Long reviewId) {
        return reviewMapper.fromReview(reviewService.getReviewById(reviewId));
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public Page<ReviewDTO> getAllReviews(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        return reviewService.getAllReviews(page, size);
    }

    @GetMapping("/room/{roomId}")
    @PreAuthorize("permitAll()")
    public Page<ReviewDTO> getRoomReviews(@PathVariable Long roomId,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        return reviewService.getRoomReviews(roomId, page, size);
    }

    @GetMapping("/guest/{guestId}")
    @PreAuthorize("hasAuthority('Admin') or @securityUtil.isGuestOwner(#guestId)")
    public Page<ReviewDTO> getGuestReviews(@PathVariable Long guestId,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        return reviewService.getGuestReviews(guestId, page, size);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Guest')")
    public ReviewDTO createReview(@RequestBody ReviewDTO reviewDTO) {
        return reviewService.createReview(reviewDTO);
    }

    @PutMapping("/{reviewId}")
    @PreAuthorize("hasAuthority('Admin') or @securityUtil.isGuestReview(#reviewId)")
    public ReviewDTO updateReview(@PathVariable Long reviewId,
                                  @RequestBody ReviewDTO reviewDTO) {
        reviewDTO.setReviewId(reviewId);
        return reviewService.updateReview(reviewDTO);
    }

    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasAuthority('Admin') or @securityUtil.isGuestReview(#reviewId)")
    public void deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
    }
}
