package com.poroflio.hotel_service.service.room;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poroflio.hotel_service.dto.RoomRequestDTO;
import com.poroflio.hotel_service.dto.RoomResponseDTO;
import com.poroflio.hotel_service.entity.Hotel;
import com.poroflio.hotel_service.entity.Room;
import com.poroflio.hotel_service.entity.RoomType;
import com.poroflio.hotel_service.exception.HotelNotFoundException;
import com.poroflio.hotel_service.exception.RoomNotFoundException;
import com.poroflio.hotel_service.exception.RoomNotFoundWithHotelId;
import com.poroflio.hotel_service.kafka.events.Room.RoomDeletedEvent;
import com.poroflio.hotel_service.kafka.events.Room.RoomRegisteredEvent;
import com.poroflio.hotel_service.kafka.events.Room.RoomUpdatedEvent;
import com.poroflio.hotel_service.kafka.services.producer.RoomEventProducer;
import com.poroflio.hotel_service.repository.HotelRepository;
import com.poroflio.hotel_service.repository.RoomRepository;
import com.poroflio.hotel_service.utils.Mapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements IRoomService {

        private final RoomRepository roomRepository;
        private final HotelRepository hotelRepository;
        private final RoomEventProducer roomEventProducer;

        /**
         * -------------------------------------------------------
         * CREATE ROOM
         * -------------------------------------------------------
         */
        @Override
        @Transactional
        public RoomResponseDTO createRoom(RoomRequestDTO dto) {

                // 1. Validate hotel existence
                Hotel hotel = hotelRepository.findById(dto.getHotelId())
                                .orElseThrow(() -> new HotelNotFoundException(
                                                "Hotel not found with ID: " + dto.getHotelId()));

                // 2. Check duplicate room number
                if (roomRepository.existsByNumberAndHotelId(dto.getNumber(), dto.getHotelId())) {
                        throw new IllegalArgumentException(
                                        "Room number '" + dto.getNumber() + "' already exists in this hotel.");
                }

                // 3. Map DTO → Entity
                Room room = Mapper.roomRequestToEntity(dto, hotel);

                // 4. Persist room (AQUÍ se genera el UUID)
                Room savedRoom = roomRepository.save(room);

                // 5. Publish Kafka event
                roomEventProducer.publishRoomRegistered(
                                new RoomRegisteredEvent(
                                                savedRoom.getId(),
                                                hotel.getId(),
                                                savedRoom.getRoomType().name()));

                // 6. Return response
                return Mapper.roomEntityToResponse(savedRoom);
        }

        /**
         * -------------------------------------------------------
         * GET ROOM BY ID
         * -------------------------------------------------------
         */
        @Override
        public RoomResponseDTO getRoomById(String id) {
                Room room = roomRepository.findById(id)
                                .orElseThrow(() -> new RoomNotFoundException(id));

                return Mapper.roomEntityToResponse(room);
        }

        /**
         * -------------------------------------------------------
         * GET AVAILABLE ROOM BY HOTEL-ID
         * -------------------------------------------------------
         */
        @Override
        public List<RoomResponseDTO> getAvailableRoomsByHotelId(String hotelId) {
                if (!hotelRepository.existsById(hotelId)) {
                        throw new HotelNotFoundException(hotelId);
                }

                List<Room> rooms = roomRepository.findByHotelIdAndIsAvailableTrue(hotelId);

                if (rooms.isEmpty()) {
                        throw new RoomNotFoundWithHotelId(hotelId);
                }

                return rooms.stream()
                                .map(Mapper::roomEntityToResponse)
                                .collect(Collectors.toList());
        }

        /**
         * -------------------------------------------------------
         * GET ROOMS BY HOTEL ID
         * -------------------------------------------------------
         */
        @Override
        public List<RoomResponseDTO> getRoomsByHotelId(String hotelId) {

                if (!hotelRepository.existsById(hotelId)) {
                        throw new HotelNotFoundException(hotelId);
                }

                List<Room> rooms = roomRepository.findByHotelId(hotelId);

                if (rooms.isEmpty()) {
                        throw new RoomNotFoundWithHotelId(hotelId);
                }

                return rooms.stream()
                                .map(Mapper::roomEntityToResponse)
                                .collect(Collectors.toList());
        }

        /**
         * -------------------------------------------------------
         * GET ROOMS BY ROOM TYPE
         * -------------------------------------------------------
         */
        @Override
        public List<RoomResponseDTO> getRoomsByRoomType(RoomType roomType) {

                List<Room> rooms = roomRepository.findByRoomType(roomType);

                if (rooms.isEmpty()) {
                        throw new RoomNotFoundException("No rooms found with type: " + roomType);
                }

                return rooms.stream()
                                .map(Mapper::roomEntityToResponse)
                                .collect(Collectors.toList());
        }

        /**
         * -------------------------------------------------------
         * UPDATE ROOM
         * -------------------------------------------------------
         */
        @Override
        @Transactional
        public RoomResponseDTO updateRoom(String id, RoomRequestDTO dto) {

                // fetch existing room
                Room room = roomRepository.findById(id)
                                .orElseThrow(() -> new RoomNotFoundException("Room not found with ID: " + id));

                // fetch associated hotel
                Hotel hotel = hotelRepository.findById(dto.getHotelId())
                                .orElseThrow(() -> new HotelNotFoundException(
                                                dto.getHotelId()));

                // validate duplicate room number within the same hotel
                boolean existsDuplicate = roomRepository.existsByNumberAndHotelId(dto.getNumber(), dto.getHotelId());

                if (existsDuplicate && !room.getNumber().equals(dto.getNumber())) {
                        throw new IllegalArgumentException(
                                        "Room number '" + dto.getNumber() + "' already exists in this hotel.");
                }

                // update room details
                room.setNumber(dto.getNumber());
                room.setHotel(hotel);
                room.setRoomType(dto.getRoomType());
                room.setAvailable(dto.getIsAvailable());

                Room updated = roomRepository.save(room);

                // event kafka emiter
                roomEventProducer.publishRoomUpdated(
                                new RoomUpdatedEvent(
                                                dto.getHotelId(),
                                                dto.getRoomType().toString()));

                return Mapper.roomEntityToResponse(updated);
        }

        @Override
        @Transactional
        public RoomResponseDTO patchRoom(String id, RoomRequestDTO dto) {
                // fetch existing room
                Room room = roomRepository.findById(id)
                                .orElseThrow(() -> new RoomNotFoundException("Room not found with ID: " + id));

                // fetch associated hotel if hotelId is provided
                if (dto.getHotelId() != null) {
                        Hotel hotel = hotelRepository.findById(dto.getHotelId())
                                        .orElseThrow(() -> new HotelNotFoundException(
                                                        dto.getHotelId()));
                        room.setHotel(hotel);
                }

                // update fields if they are provided in the DTO
                if (dto.getNumber() != null) {
                        // validate duplicate room number within the same hotel
                        boolean existsDuplicate = roomRepository.existsByNumberAndHotelId(dto.getNumber(),
                                        room.getHotel().getId());

                        if (existsDuplicate && !room.getNumber().equals(dto.getNumber())) {
                                throw new IllegalArgumentException(
                                                "Room number '" + dto.getNumber() + "' already exists in this hotel.");
                        }
                        room.setNumber(dto.getNumber());
                }
                if (dto.getRoomType() != null) {
                        room.setRoomType(dto.getRoomType());
                }
                if (dto.getIsAvailable() != null) {
                        room.setAvailable(dto.getIsAvailable());
                }

                Room patched = roomRepository.save(room);

                return Mapper.roomEntityToResponse(patched);
        }

        /**
         * -------------------------------------------------------
         * DELETE ROOM
         * -------------------------------------------------------
         */
        @Override
        public void deleteRoom(String id) {
                if (!roomRepository.existsById(id)) {
                        throw new RoomNotFoundException("Room not found with ID: " + id);
                }

                roomRepository.deleteById(id);

                // kafka event for delete-room
                roomEventProducer.deleteRoom(
                                new RoomDeletedEvent(id));
        }

}