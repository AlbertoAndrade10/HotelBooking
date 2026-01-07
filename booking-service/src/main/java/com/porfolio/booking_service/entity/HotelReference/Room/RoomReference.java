package com.porfolio.booking_service.entity.HotelReference.Room;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "rooms_references")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RoomReference {
    @Id
    private String UUID;

    @Column(name = "hotel_UUID", nullable = false)
    private String hotelUUID;
    private String roomType;
}
