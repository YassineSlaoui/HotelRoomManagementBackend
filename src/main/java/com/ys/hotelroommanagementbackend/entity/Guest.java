package com.ys.hotelroommanagementbackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "guests")
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guest_id")
    private long guestId;

    @Basic
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Basic
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Basic
    @Column(name = "contact_info", nullable = false)
    private String contactInfo;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "guest", cascade = CascadeType.REMOVE)
    private Set<Reservation> reservations = new HashSet<>();

    @Override
    public String toString() {
        return "Guest{" +
                "guestId=" + guestId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guest guest = (Guest) o;
        return guestId == guest.guestId && Objects.equals(firstName, guest.firstName) && Objects.equals(lastName, guest.lastName) && Objects.equals(contactInfo, guest.contactInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guestId, firstName, lastName, contactInfo);
    }

}
