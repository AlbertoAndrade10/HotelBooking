package com.porfolio.booking_service.kafka.events.booking;

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
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreatedEvent {
    private String bookingUUID;
    private String userUUID;
    private String hotelUUID;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private BigDecimal totalPrice;
    private BookingStatus bookingStatus;
    private LocalDateTime createdAt;
}
