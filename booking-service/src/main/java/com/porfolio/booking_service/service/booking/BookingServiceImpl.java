package com.porfolio.booking_service.service.booking;

import java.util.List;

import org.springframework.stereotype.Service;

import com.porfolio.booking_service.dto.BookingResponse;
import com.porfolio.booking_service.dto.CreateBookingRequest;
import com.porfolio.booking_service.entity.Booking.Booking;
import com.porfolio.booking_service.entity.Booking.BookingStatus;
import com.porfolio.booking_service.exception.booking.BookingNotFoundException;
import com.porfolio.booking_service.kafka.service.producer.BookingEventProducer;
import com.porfolio.booking_service.repository.Booking.BookingRepository;
import com.porfolio.booking_service.repository.Hotel.HotelReferenceRepository;
import com.porfolio.booking_service.repository.Hotel.RoomReferenceRepository;
import com.porfolio.booking_service.utils.BookingStateMachine;
import com.porfolio.booking_service.utils.Mapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements IBookingService {
    private final BookingRepository bookingRepository;
    private final HotelReferenceRepository hotelReferenceRepository;
    private final RoomReferenceRepository roomReferenceRepository;
    private final BookingEventProducer bookingEventProducer;

    @Override
    public BookingResponse createBooking(String userId, CreateBookingRequest request) {
        // 1-> validate room exists in hotel-reference-service

        // 2-> validate availability

        // 3-> create booking

        // 4-> save booking

        // 3-> event kafka

        // 6-> return response
        return null;

    }

    @Override
    public List<BookingResponse> getAllBookings() {

        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream().map(Mapper::toResponseDto).toList();
    }

    @Override
    public BookingResponse getBookingByUUID(String uuid) {
        Booking booking = bookingRepository.findById(uuid)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id: " + uuid));
        return Mapper.toResponseDto(booking);
    }

    @Override
    public List<BookingResponse> getBookingsByUserUUID(String userUUID) {
        List<Booking> bookings = bookingRepository.findByUserUUID(userUUID);
        return bookings.stream().map(Mapper::toResponseDto).toList();
    }

    @Override
    public List<BookingResponse> getBookingsListByStatus(String status) {
        List<Booking> bookings = bookingRepository.findByBookingStatus(status);
        return Mapper.toResponseDtoList(bookings);
    }

    @Override
    public BookingResponse updateStatus(String bookingUUID, String status) {

        // step1 -> comprobar si existe ese booking
        Booking booking = bookingRepository.findByUUID(bookingUUID)
                .orElseThrow(() -> new BookingNotFoundException(bookingUUID));

        /**
         * step2 -> Cambiar el estado al que recibe por parametro
         */
        BookingStatus newStatus = BookingStatus.valueOf(status);

        // validate state machine
        BookingStateMachine.validateTransition(booking.getBookingStatus(), newStatus);

        booking.setBookingStatus(newStatus);

        // step3 -> guardar el cambio en la base de datos

        bookingRepository.save(booking);

        return Mapper.toResponseDto(booking);
    }

    @Override
    public BookingResponse deleteBooking(String bookingUUID) {

        // step1 -> comprobar si existe ese booking
        Booking booking = bookingRepository.findByUUID(bookingUUID)
                .orElseThrow(() -> new BookingNotFoundException(bookingUUID));

        // step2 -> cambiar a FINISHED solo si la transicion es valida
        BookingStateMachine.validateTransition(booking.getBookingStatus(), BookingStatus.FINISHED);
        booking.setBookingStatus(BookingStatus.FINISHED);

        // step3 -> Actualizarlo en la base de datos
        bookingRepository.save(booking);

        return Mapper.toResponseDto(booking);

    }

}
