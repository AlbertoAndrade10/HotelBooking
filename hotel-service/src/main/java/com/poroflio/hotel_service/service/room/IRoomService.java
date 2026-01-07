package com.poroflio.hotel_service.service.room;

import java.util.List;

import com.poroflio.hotel_service.dto.RoomRequestDTO;
import com.poroflio.hotel_service.dto.RoomResponseDTO;
import com.poroflio.hotel_service.entity.RoomType;

/**
 * CRUD methods for Room entity
 */

public interface IRoomService {

    RoomResponseDTO createRoom(RoomRequestDTO dto);

    RoomResponseDTO getRoomById(String id);

    List<RoomResponseDTO> getRoomsByHotelId(String hotelId);

    List<RoomResponseDTO> getRoomsByRoomType(RoomType roomType);

    RoomResponseDTO updateRoom(String id, RoomRequestDTO dto);

    RoomResponseDTO patchRoom(String id, RoomRequestDTO dto);

    List<RoomResponseDTO> getAvailableRoomsByHotelId(String hotelId);

    void deleteRoom(String id);

}
