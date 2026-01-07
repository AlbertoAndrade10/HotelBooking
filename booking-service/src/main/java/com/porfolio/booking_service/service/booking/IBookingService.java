package com.porfolio.booking_service.service.booking;

import java.util.List;

import com.porfolio.booking_service.dto.BookingResponse;
import com.porfolio.booking_service.dto.CreateBookingRequest;

public interface IBookingService {

    // CREATE
    BookingResponse createBooking(String userId, CreateBookingRequest request);

    // GET
    BookingResponse getBookingByUUID(String uuid);

    List<BookingResponse> getAllBookings();

    List<BookingResponse> getBookingsByUserUUID(String userUUID);

    List<BookingResponse> getBookingsListByStatus(String status);

    // PATCH / UPDATE
    BookingResponse updateStatus(String bookingUUID, String status);

    // DELETE (que en realidad no se borra si no que se cambia a FINISHED porq ese
    // Booking ya ha finalizado y la persona ha abandonado la habitacion de hotel)
    BookingResponse deleteBooking(String bookingUUID);
}
