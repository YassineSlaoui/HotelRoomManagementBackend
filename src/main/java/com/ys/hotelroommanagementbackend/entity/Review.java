package com.ys.hotelroommanagementbackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;
    @Basic
    @Column(name = "rating")
    private Integer rating;
    @Basic
    @Column(name = "comment")
    private String comment;
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "reservation_id", referencedColumnName = "reservation_id", nullable = false)
    private Reservation reservation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(reviewId, review.reviewId) && Objects.equals(rating, review.rating) && Objects.equals(comment, review.comment) && Objects.equals(reservation, review.reservation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviewId, rating, comment, reservation);
    }
}
