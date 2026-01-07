package com.poroflio.hotel_service.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String number;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType roomType;

    private boolean isAvailable;

    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private BigDecimal pricePerNight;

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
        this.pricePerNight = roomType.getPricePerNight();
    }
}
