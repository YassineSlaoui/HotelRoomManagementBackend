package com.ys.hotelroommanagementbackend.web;

import com.ys.hotelroommanagementbackend.dto.ReviewDTO;
import com.ys.hotelroommanagementbackend.mapper.ReviewMapper;
import com.ys.hotelroommanagementbackend.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reviews")
@CrossOrigin("*")
@SecurityRequirement(name = "Access Token Authorization")
public class ReviewRestController {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    public ReviewRestController(ReviewService reviewService, ReviewMapper reviewMapper) {
        this.reviewService = reviewService;
        this.reviewMapper = reviewMapper;
    }

    @Operation(summary = "Get review by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error"),
            @ApiResponse(responseCode = "403", description = "Access to this resource is forbidden",
                    content = @Content(schema = @Schema(type = "object", implementation = Map.class,
                            properties = {
                                    @StringToClassMapItem(key = "timestamp", value = Date.class),
                                    @StringToClassMapItem(key = "status", value = Integer.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                            }))),
            @ApiResponse(responseCode = "404", description = "Requested resource not found")
    })
    @GetMapping("/{reviewId}")
    @PreAuthorize("permitAll()")
    public ReviewDTO getReviewById(@PathVariable Long reviewId) {
        return reviewMapper.fromReview(reviewService.getReviewById(reviewId));
    }

    @Operation(summary = "Get all reviews")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error"),
            @ApiResponse(responseCode = "403", description = "Access to this resource is forbidden",
                    content = @Content(schema = @Schema(type = "object", implementation = Map.class,
                            properties = {
                                    @StringToClassMapItem(key = "timestamp", value = Date.class),
                                    @StringToClassMapItem(key = "status", value = Integer.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                            })))
    })
    @GetMapping
    @PreAuthorize("permitAll()")
    public Page<ReviewDTO> getAllReviews(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        return reviewService.getAllReviews(page, size);
    }

    @Operation(summary = "Get room reviews")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error"),
            @ApiResponse(responseCode = "403", description = "Access to this resource is forbidden",
                    content = @Content(schema = @Schema(type = "object", implementation = Map.class,
                            properties = {
                                    @StringToClassMapItem(key = "timestamp", value = Date.class),
                                    @StringToClassMapItem(key = "status", value = Integer.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                            })))
    })
    @GetMapping("/room/{roomId}")
    @PreAuthorize("permitAll()")
    public Page<ReviewDTO> getRoomReviews(@PathVariable Long roomId,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        return reviewService.getRoomReviews(roomId, page, size);
    }

    @Operation(summary = "Get guest reviews")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error"),
            @ApiResponse(responseCode = "403", description = "Access to this resource is forbidden",
                    content = @Content(schema = @Schema(type = "object", implementation = Map.class,
                            properties = {
                                    @StringToClassMapItem(key = "timestamp", value = Date.class),
                                    @StringToClassMapItem(key = "status", value = Integer.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                            })))
    })
    @GetMapping("/guest/{guestId}")
    @PreAuthorize("hasAuthority('Admin') or @securityUtil.isGuestOwner(#guestId)")
    public Page<ReviewDTO> getGuestReviews(@PathVariable Long guestId,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        return reviewService.getGuestReviews(guestId, page, size);
    }

    @Operation(summary = "Create a review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error"),
            @ApiResponse(responseCode = "403", description = "Access to this resource is forbidden",
                    content = @Content(schema = @Schema(type = "object", implementation = Map.class,
                            properties = {
                                    @StringToClassMapItem(key = "timestamp", value = Date.class),
                                    @StringToClassMapItem(key = "status", value = Integer.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                            })))
    })
    @PostMapping
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Guest')")
    public ReviewDTO createReview(@RequestBody ReviewDTO reviewDTO) {
        return reviewService.createReview(reviewDTO);
    }

    @Operation(summary = "Update a review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error"),
            @ApiResponse(responseCode = "403", description = "Access to this resource is forbidden",
                    content = @Content(schema = @Schema(type = "object", implementation = Map.class,
                            properties = {
                                    @StringToClassMapItem(key = "timestamp", value = Date.class),
                                    @StringToClassMapItem(key = "status", value = Integer.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                            }))),
            @ApiResponse(responseCode = "404", description = "Requested resource not found")
    })
    @PutMapping("/{reviewId}")
    @PreAuthorize("hasAuthority('Admin') or @securityUtil.isGuestReview(#reviewId)")
    public ReviewDTO updateReview(@PathVariable Long reviewId,
                                  @RequestBody ReviewDTO reviewDTO) {
        reviewDTO.setReviewId(reviewId);
        return reviewService.updateReview(reviewDTO);
    }

    @Operation(summary = "Delete a review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error"),
            @ApiResponse(responseCode = "403", description = "Access to this resource is forbidden",
                    content = @Content(schema = @Schema(type = "object", implementation = Map.class,
                            properties = {
                                    @StringToClassMapItem(key = "timestamp", value = Date.class),
                                    @StringToClassMapItem(key = "status", value = Integer.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                            }))),
            @ApiResponse(responseCode = "404", description = "Requested resource not found")
    })
    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasAuthority('Admin') or @securityUtil.isGuestReview(#reviewId)")
    public void deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
    }
}
