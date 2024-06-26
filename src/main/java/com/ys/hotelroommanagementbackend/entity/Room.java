package com.ys.hotelroommanagementbackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    @Basic
    @Column(name = "room_number", nullable = false)
    private String roomNumber;

    @Basic
    @Column(name = "room_type", nullable = false)
    private String roomType;

    @Basic
    @Column(name = "description", nullable = false)
    private String description;

    @Basic
    @Column(name = "price", nullable = false)
    private Double price;

    @Basic
    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Basic
    @Column(name = "available", nullable = false)
    private Boolean available;

    @Basic
    @Column(name = "maintenance", nullable = false)
    private Boolean maintenance;

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> photoURLs = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE)
    private Set<Reservation> reservations = new HashSet<>();

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", roomNumber='" + roomNumber + '\'' +
                ", roomType='" + roomType + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", capacity=" + capacity +
                ", available=" + available +
                ", maintenance=" + maintenance +
                ", photoURLs=" + photoURLs +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(roomId, room.roomId) && Objects.equals(roomNumber, room.roomNumber) && Objects.equals(roomType, room.roomType) && Objects.equals(description, room.description) && Objects.equals(price, room.price) && Objects.equals(capacity, room.capacity) && Objects.equals(available, room.available) && Objects.equals(photoURLs, room.photoURLs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId, roomNumber, roomType, description, price, capacity, available, photoURLs);
    }

}
