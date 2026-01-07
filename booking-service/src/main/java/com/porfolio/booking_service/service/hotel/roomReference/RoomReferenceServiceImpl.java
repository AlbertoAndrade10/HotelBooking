package com.porfolio.booking_service.service.hotel.roomReference;

import org.springframework.stereotype.Service;

import com.porfolio.booking_service.entity.HotelReference.Room.RoomReference;
import com.porfolio.booking_service.exception.room.RoomAlreadyExistsException;
import com.porfolio.booking_service.exception.room.RoomNotFoundException;
import com.porfolio.booking_service.kafka.events.roomReference.RoomDeletedEvent;
import com.porfolio.booking_service.kafka.events.roomReference.RoomRegisteredEvent;
import com.porfolio.booking_service.kafka.events.roomReference.RoomUpdatedEvent;
import com.porfolio.booking_service.repository.Hotel.RoomReferenceRepository;

@Service
public class RoomReferenceServiceImpl implements IRoomReferenceService {
    private final RoomReferenceRepository roomReferenceRepository;

    public RoomReferenceServiceImpl(RoomReferenceRepository roomReferenceRepository) {
        this.roomReferenceRepository = roomReferenceRepository;
    }

    // registered room reference by kafka
    @Override
    public void registeredRoom(RoomRegisteredEvent event) {
        if (roomReferenceRepository.existsById(event.getRoomUUID())) {
            throw new RoomAlreadyExistsException("Room already exists with UUID: " + event.getRoomUUID());
        }
        RoomReference ref = new RoomReference();
        ref.setUUID(event.getRoomUUID());
        ref.setHotelUUID(event.getHotelUUID());
        ref.setRoomType(event.getRoomType());
        roomReferenceRepository.save(ref);
        System.out.println("Datos que me vienen desde evento de kafka: " + ref.toString());
    }

    // update room reference by kafka
    @Override
    public void updatedRoom(RoomUpdatedEvent event) {
        RoomReference ref = roomReferenceRepository.findById(event.getUUID())
                .orElseThrow(() -> new RoomNotFoundException("Room not found with UUID: " + event.getUUID()));

        ref.setUUID(event.getUUID());
        ref.setHotelUUID(event.getHotelUUID());
        ref.setRoomType(event.getRoomType());
    }

    // delete room reference by kafka
    @Override
    public void deleteRoom(RoomDeletedEvent event) {
        RoomReference ref = roomReferenceRepository.findById(event.getUUID())
                .orElseThrow(() -> new RoomNotFoundException("Room not found with UUID: " + event.getUUID()));

        roomReferenceRepository.delete(ref);
        System.out.println("RoomRef with UUID: " + event.getUUID() + " has been deleted");
    }

}
