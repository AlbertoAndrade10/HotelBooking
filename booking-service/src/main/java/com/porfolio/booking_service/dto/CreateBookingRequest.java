package com.porfolio.booking_service.dto;

import java.math.BigDecimal;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.porfolio.booking_service.entity.HotelReference.Room.RoomType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingRequest {
    private String hotelUUID;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate checkInDate;

    private RoomType roomType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate checkOutDate;
    private BigDecimal totalPrice;

}
