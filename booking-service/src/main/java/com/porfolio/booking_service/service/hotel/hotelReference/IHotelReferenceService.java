package com.porfolio.booking_service.service.hotel.hotelReference;

import com.porfolio.booking_service.kafka.events.hotelReference.HotelDeletedEvent;
import com.porfolio.booking_service.kafka.events.hotelReference.HotelRegisteredEvent;
import com.porfolio.booking_service.kafka.events.hotelReference.HotelUpdatedEvent;

public interface IHotelReferenceService {
    void registerHotel(HotelRegisteredEvent event);

    void updateHotel(HotelUpdatedEvent event);

    void deleteHotel(HotelDeletedEvent event);
}
