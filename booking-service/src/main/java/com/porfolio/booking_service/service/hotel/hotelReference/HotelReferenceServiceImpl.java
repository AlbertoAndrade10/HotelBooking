package com.porfolio.booking_service.service.hotel.hotelReference;

import org.springframework.stereotype.Service;

import com.porfolio.booking_service.entity.HotelReference.Hotel.HotelReference;
import com.porfolio.booking_service.exception.hotel.HotelAlreadyExistsException;
import com.porfolio.booking_service.exception.hotel.HotelNotFoundException;
import com.porfolio.booking_service.kafka.events.hotelReference.HotelDeletedEvent;
import com.porfolio.booking_service.kafka.events.hotelReference.HotelRegisteredEvent;
import com.porfolio.booking_service.kafka.events.hotelReference.HotelUpdatedEvent;
import com.porfolio.booking_service.repository.Hotel.HotelReferenceRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class HotelReferenceServiceImpl implements IHotelReferenceService {
    private final HotelReferenceRepository hotelReferenceRepository;

    public HotelReferenceServiceImpl(HotelReferenceRepository hotelReferenceRepository) {
        this.hotelReferenceRepository = hotelReferenceRepository;
    }

    // register hotel reference by kafka
    @Override
    public void registerHotel(HotelRegisteredEvent event) {
        if (hotelReferenceRepository.existsById(event.getHotelId())) {
            throw new HotelAlreadyExistsException("Hotel already exists with UUID: " + event.getHotelId());
        }
        HotelReference ref = new HotelReference();
        ref.setHotelId(event.getHotelId());
        ref.setName(event.getName());
        ref.setActive(true);
        hotelReferenceRepository.save(ref);

    }

    // update hotel referece by kafka
    @Override
    public void updateHotel(HotelUpdatedEvent event) {
        HotelReference ref = hotelReferenceRepository.findById(event.getHotelId())
                .orElseThrow(() -> new HotelNotFoundException("Hotel not found with UUID: " + event.getHotelId()));
        ref.setName(event.getName());

    }

    // delete hotel reference by kafka
    @Override
    public void deleteHotel(HotelDeletedEvent event) {
        HotelReference ref = hotelReferenceRepository.findById(event.getHotelUUID())
                .orElseThrow(() -> new HotelNotFoundException("Hotel not found with UUID: " + event.getHotelUUID()));

        hotelReferenceRepository.delete(ref);
        System.out.println("HotelRef with UUID: " + event.getHotelUUID() + " has been deleted");
    }

}
