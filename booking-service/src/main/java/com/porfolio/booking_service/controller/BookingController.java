package com.porfolio.booking_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.porfolio.booking_service.dto.BookingResponse;
import com.porfolio.booking_service.dto.CreateBookingRequest;
import com.porfolio.booking_service.entity.Booking.BookingStatus;
import com.porfolio.booking_service.exception.ForbiddenOperationException;
import com.porfolio.booking_service.service.booking.IBookingService;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final IBookingService bookingService;

    public BookingController(IBookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Role") String role,
            @RequestBody CreateBookingRequest request) {

        System.out.println("[BookingController] userId header: " + userId);
        System.out.println("[BookingController] role header: " + role);

        BookingResponse response = bookingService.createBooking(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings(
            @RequestHeader("X-User-Role") String role) {

        if (!"ADMIN".equals(role)) {
            throw new ForbiddenOperationException();
        }

        List<BookingResponse> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<BookingResponse> getBookingByUUID(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Role") String role,
            @PathVariable String uuid) {

        BookingResponse booking = bookingService.getBookingByUUID(uuid);

        // Si es USER, solo puede ver sus propias reservas
        if ("USER".equals(role) && !booking.getUserUUID().equals(userId)) {
            throw new ForbiddenOperationException();
        }

        return ResponseEntity.ok(booking);
    }

    @GetMapping("/user/{userUUID}")
    public ResponseEntity<List<BookingResponse>> getBookingsByUserUUID(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Role") String role,
            @PathVariable String userUUID) {

        if ("USER".equals(role) && !userId.equals(userUUID)) {
            throw new ForbiddenOperationException();
        }

        List<BookingResponse> bookings = bookingService.getBookingsByUserUUID(userUUID);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BookingResponse>> getBookingsByStatus(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Role") String role,
            @PathVariable BookingStatus status) {

        List<BookingResponse> bookings;

        if ("ADMIN".equals(role)) {
            bookings = bookingService.getBookingsListByStatus(status.name());
        } else {
            // USER solo puede ver sus propias reservas con ese status
            bookings = bookingService.getBookingsByUserUUID(userId).stream()
                    .filter(b -> b.getBookingStatus() == status)
                    .toList();
        }

        return ResponseEntity.ok(bookings);
    }
}
