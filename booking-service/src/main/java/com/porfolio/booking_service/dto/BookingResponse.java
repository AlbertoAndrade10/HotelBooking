package com.porfolio.booking_service.dto;

import java.math.BigDecimal;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.porfolio.booking_service.entity.Booking.BookingStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private String uuid;
    private String userUUID;
    private String hotelUUID;
    private LocalDateTime createdAt;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private BigDecimal totalPrice;
    private BookingStatus bookingStatus;
}
