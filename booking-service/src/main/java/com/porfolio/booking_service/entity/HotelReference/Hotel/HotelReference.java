package com.porfolio.booking_service.entity.HotelReference.Hotel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "hotel_references")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotelReference {
    @Id
    @Column(name = "hotel_id", nullable = false)
    private String hotelId;

    @Column(nullable = false)
    private boolean active;

    private String name;
}
