package com.porfolio.booking_service.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class BookingTimeCalculator {
    private static final LocalTime CHECK_IN_TIME = LocalTime.of(12, 0);

    private BookingTimeCalculator() {
    }

    public static LocalDateTime calculateStartAt(LocalDate checkInDate) {
        return LocalDateTime.of(checkInDate, CHECK_IN_TIME);
    }

    public static LocalDateTime calculateEndAt(LocalDate checkOutDate) {
        return LocalDateTime.of(checkOutDate.plusDays(1), CHECK_IN_TIME);
    }
}
