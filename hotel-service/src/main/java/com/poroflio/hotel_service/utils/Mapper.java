package com.poroflio.hotel_service.utils;

import java.util.List;
import java.util.stream.Collectors;

import com.poroflio.hotel_service.dto.HotelRequestDTO;
import com.poroflio.hotel_service.dto.HotelResponseDTO;
import com.poroflio.hotel_service.dto.RoomRequestDTO;
import com.poroflio.hotel_service.dto.RoomResponseDTO;
import com.poroflio.hotel_service.entity.Hotel;
import com.poroflio.hotel_service.entity.Room;

public class Mapper {

    /**
     * -----------------------------------
     * Hotel Mappers
     * -----------------------------------
     */

    public static Hotel hotelRequetToEntity(HotelRequestDTO dto) {
        Hotel hotel = new Hotel();
        hotel.setName(dto.getName());
        hotel.setAddress(dto.getAddress());
        hotel.setEmail(dto.getEmail());
        hotel.setPhoneNumber(dto.getPhoneNumber());
        hotel.setDescription(dto.getDescription());
        return hotel;
    }

    public static HotelResponseDTO hotelEntityToResponse(Hotel hotel) {
        HotelResponseDTO dto = new HotelResponseDTO();
        dto.setId(hotel.getId());
        dto.setName(hotel.getName());
        dto.setAddress(hotel.getAddress());
        dto.setEmail(hotel.getEmail());
        dto.setPhoneNumber(hotel.getPhoneNumber());
        dto.setDescription(hotel.getDescription());

        if (hotel.getRooms() != null) {
            List<RoomResponseDTO> roomDTOs = hotel.getRooms()
                    .stream()
                    .map(Mapper::roomEntityToResponse)
                    .collect(Collectors.toList());

            dto.setRooms(roomDTOs);
        }
        return dto;
    }

    /**
     * -----------------------------------
     * Room Mappers
     * -----------------------------------
     */

    public static Room roomRequestToEntity(RoomRequestDTO dto, Hotel hotel) {
        Room room = new Room();
        room.setNumber(dto.getNumber());
        room.setHotel(hotel); // Set the associated hotel
        room.setRoomType(dto.getRoomType());
        room.setAvailable(dto.getIsAvailable());
        return room;

    }

    public static RoomResponseDTO roomEntityToResponse(Room room) {
        RoomResponseDTO dto = new RoomResponseDTO();
        dto.setId(room.getId());
        dto.setNumber(room.getNumber());
        dto.setRoomType(room.getRoomType().name());
        dto.setIsAvailable(room.isAvailable());
        dto.setPricePerNight(room.getPricePerNight());
        return dto;
    }

}
