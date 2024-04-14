package com.ys.hotelroommanagementbackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id", referencedColumnName = "guest_id", nullable = false)
    private Guest guest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", referencedColumnName = "room_id", nullable = false)
    private Room room;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "review_id", referencedColumnName = "review_id")
    private Review review;

    @Basic
    @Column(name = "check_in_date")
    private Date checkInDate;

    @Basic
    @Column(name = "check_out_date")
    private Date checkOutDate;

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId=" + reservationId +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(reservationId, that.reservationId) && Objects.equals(checkInDate, that.checkInDate) && Objects.equals(checkOutDate, that.checkOutDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId, checkInDate, checkOutDate);
    }

}

