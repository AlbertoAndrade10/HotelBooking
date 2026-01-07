package com.porfolio.booking_service.utils;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import com.porfolio.booking_service.entity.Booking.BookingStatus;
import com.porfolio.booking_service.exception.booking.InvalidBookingException;

public class BookingStateMachine {

    // Define allowed transitions
    private static final Map<BookingStatus, Set<BookingStatus>> transitions = new EnumMap<>(BookingStatus.class);

    static {
        transitions.put(BookingStatus.PENDING,
                EnumSet.of(BookingStatus.CONFIRMED, BookingStatus.CANCELLED, BookingStatus.FINISHED));
        transitions.put(BookingStatus.CONFIRMED, EnumSet.of(BookingStatus.CANCELLED, BookingStatus.FINISHED));
        transitions.put(BookingStatus.CANCELLED, EnumSet.noneOf(BookingStatus.class)); // terminal
        transitions.put(BookingStatus.FINISHED, EnumSet.noneOf(BookingStatus.class)); // terminal
    }

    public static void validateTransition(BookingStatus from, BookingStatus to) {
        if (!transitions.get(from).contains(to)) {
            throw new InvalidBookingException("cannot transition booking from " + from + " to " + to);
        }
    }

}
