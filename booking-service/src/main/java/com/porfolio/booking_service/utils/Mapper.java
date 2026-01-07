package com.porfolio.booking_service.utils;

import java.util.List;
import java.util.stream.Collectors;

import com.porfolio.booking_service.dto.BookingResponse;
import com.porfolio.booking_service.entity.Booking.Booking;

public class Mapper {

    public static BookingResponse toResponseDto(Booking booking) {
        if (booking == null)
            return null;

        return new BookingResponse(
                booking.getUUID(),
                booking.getUserUUID(),
                booking.getHotelUUID(),
                booking.getCreatedAt(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getTotalPrice(),
                booking.getBookingStatus());
    }

    public static List<BookingResponse> toResponseDtoList(List<Booking> bookings) {
        if (bookings == null || bookings.isEmpty())
            return List.of();

        return bookings.stream()
                .map(Mapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
