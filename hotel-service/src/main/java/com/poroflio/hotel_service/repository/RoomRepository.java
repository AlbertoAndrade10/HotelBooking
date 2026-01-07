package com.poroflio.hotel_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poroflio.hotel_service.entity.Room;
import com.poroflio.hotel_service.entity.RoomType;

public interface RoomRepository extends JpaRepository<Room, String> {
    
    List<Room> findByHotelId(String hotelId);

    List<Room> findByHotelIdAndIsAvailableTrue(String hotelId);

    List<Room> findByRoomType(RoomType roomType);

    boolean existsByNumberAndHotelId(String number, String hotelId);

}
