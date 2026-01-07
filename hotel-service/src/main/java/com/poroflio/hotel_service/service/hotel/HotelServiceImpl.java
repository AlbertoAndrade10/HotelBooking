package com.poroflio.hotel_service.service.hotel;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.poroflio.hotel_service.dto.HotelRequestDTO;
import com.poroflio.hotel_service.dto.HotelResponseDTO;
import com.poroflio.hotel_service.entity.Hotel;
import com.poroflio.hotel_service.exception.EmptyListException;
import com.poroflio.hotel_service.exception.HotelAlreadyExistsException;
import com.poroflio.hotel_service.exception.HotelNotFoundException;
import com.poroflio.hotel_service.kafka.events.Hotel.HotelDeletedEvent;
import com.poroflio.hotel_service.kafka.events.Hotel.HotelRegisteredEvent;
import com.poroflio.hotel_service.kafka.events.Hotel.HotelUpdatedEvent;
import com.poroflio.hotel_service.kafka.services.producer.HotelEventProducer;
import com.poroflio.hotel_service.repository.HotelRepository;
import com.poroflio.hotel_service.utils.Mapper;

@Service
public class HotelServiceImpl implements IHotelService {

    private final HotelRepository hotelRepository;
    private final HotelEventProducer hotelEventProducer;

    public HotelServiceImpl(HotelRepository hotelRepository, HotelEventProducer hotelEventProducer) {
        this.hotelRepository = hotelRepository;
        this.hotelEventProducer = hotelEventProducer;

    }

    @Override
    public List<HotelResponseDTO> getAllHotels() {
        List<Hotel> hotels = hotelRepository.findAll();

        if (hotels.isEmpty()) {
            throw new EmptyListException();
        }

        return hotels.stream()
                .map(Mapper::hotelEntityToResponse)
                .collect(Collectors.toList());

    }

    @Override
    public HotelResponseDTO getHotelById(String hotelId) {

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException(hotelId));

        return Mapper.hotelEntityToResponse(hotel);
    }

    @Override
    public HotelResponseDTO getHotelByName(String name) {
        Hotel hotel = hotelRepository.findByName(name)
                .orElseThrow(() -> new HotelNotFoundException(name));

        return Mapper.hotelEntityToResponse(hotel);
    }

    @Override
    public HotelResponseDTO createHotel(HotelRequestDTO hotelRequestDTO) {
        Hotel hotel = Mapper.hotelRequetToEntity(hotelRequestDTO);

        if (hotelRepository.existsByName(hotel.getName())) {
            throw new HotelAlreadyExistsException("Hotel with name " + hotel.getName() + " already exists");

        }
        Hotel savedHotel = hotelRepository.save(hotel);

        // kafka event publish hotel registered
        hotelEventProducer.publishHotelRegistered(
                new HotelRegisteredEvent(
                        savedHotel.getId(),
                        savedHotel.getName()));

        System.out.println("Se ha enviado el evento hotel-registered para el hotelId: " + savedHotel.getId());

        HotelResponseDTO response = Mapper.hotelEntityToResponse(savedHotel);

        System.out.println("Datos de CREATE HOTEL para el evento de Kafka" + response.toString());
        return response;

    }

    @Override
    public HotelResponseDTO updateHotel(String hotelId, HotelRequestDTO hotelRequestDTO) {
        Hotel existingHotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException(hotelId));

        existingHotel.setName(hotelRequestDTO.getName());
        existingHotel.setAddress(hotelRequestDTO.getAddress());
        existingHotel.setEmail(hotelRequestDTO.getEmail());
        existingHotel.setPhoneNumber(hotelRequestDTO.getPhoneNumber());
        existingHotel.setDescription(hotelRequestDTO.getDescription());

        Hotel updatedHotel = hotelRepository.save(existingHotel);

        // kafka event publish hotel updated
        hotelEventProducer.publishHotelUpdated(
                new HotelUpdatedEvent(
                        updatedHotel.getId(),
                        updatedHotel.getName())

        );

        return Mapper.hotelEntityToResponse(updatedHotel);
    }

    @Override
    public HotelResponseDTO updateHotelPartial(String hotelId, HotelRequestDTO hotelRequestDTO) {
        Hotel exisHotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException(hotelId));
        if (hotelRequestDTO.getName() != null) {
            exisHotel.setName(hotelRequestDTO.getName());
        }
        if (hotelRequestDTO.getAddress() != null) {
            exisHotel.setAddress(hotelRequestDTO.getAddress());
        }
        if (hotelRequestDTO.getEmail() != null) {
            exisHotel.setEmail(hotelRequestDTO.getEmail());
        }
        if (hotelRequestDTO.getPhoneNumber() != null) {
            exisHotel.setPhoneNumber(hotelRequestDTO.getPhoneNumber());
        }
        if (hotelRequestDTO.getDescription() != null) {
            exisHotel.setDescription(hotelRequestDTO.getDescription());
        }
        Hotel updatedHotel = hotelRepository.save(exisHotel);

        hotelEventProducer.publishHotelUpdated(
                new HotelUpdatedEvent(
                        updatedHotel.getId(),
                        updatedHotel.getName())

        );

        return Mapper.hotelEntityToResponse(updatedHotel);
    }

    @Override
    public void deleteHotel(String hotelId) {
        Hotel existingHotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException(hotelId));

        hotelRepository.delete(existingHotel);

        // event for deleted-hotel
        hotelEventProducer.deletedHotel(
                new HotelDeletedEvent());
    }

}
