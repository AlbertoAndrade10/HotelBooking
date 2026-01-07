package com.porfolio.booking_service.repository.Hotel;



import org.springframework.data.jpa.repository.JpaRepository;

import com.porfolio.booking_service.entity.HotelReference.Room.RoomReference;

public interface RoomReferenceRepository extends JpaRepository<RoomReference, String> {
    
}
