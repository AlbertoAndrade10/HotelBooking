package com.poroflio.hotel_service.service.hotel;

import java.util.List;

import com.poroflio.hotel_service.dto.HotelRequestDTO;
import com.poroflio.hotel_service.dto.HotelResponseDTO;

/**
 * CRUD methods for Hotel entity
 */
public interface IHotelService {
    // RESPONSES
    List<HotelResponseDTO> getAllHotels();

    HotelResponseDTO getHotelById(String hotelId);

    HotelResponseDTO getHotelByName(String name);
    
    HotelResponseDTO updateHotelPartial(String hotelId, HotelRequestDTO hotelRequestDTO);
    // REQUESTS
    HotelResponseDTO createHotel(HotelRequestDTO hotelRequestDTO);

    HotelResponseDTO updateHotel(String hotelId, HotelRequestDTO hotelRequestDTO);


    void deleteHotel(String hotelId);

}
