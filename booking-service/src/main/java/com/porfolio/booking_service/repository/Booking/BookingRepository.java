package com.porfolio.booking_service.repository.Booking;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.porfolio.booking_service.entity.Booking.Booking;

public interface BookingRepository extends JpaRepository<Booking, String> {

        List<Booking> findByUserUUID(String userUUID);

        List<Booking> findByBookingStatus(String status);

        Optional<Booking> findByUUID(String uuid);

        // custom query to check for overlapping bookings
        @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.hotelUUID = :hotelUUID AND b.bookingStatus IN ('CONFIRMED', 'PENDING') "
                        +
                        "AND ((b.checkInDate <= :checkOutDate) AND (b.checkOutDate >= :checkInDate))")
        boolean existsActiveBookingOverlapping(
                        @Param("hotelUUID") String hotelUUID,
                        @Param("checkInDate") LocalDate checkInDate,
                        @Param("checkOutDate") LocalDate checkOutDate);

}
