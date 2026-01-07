package com.poroflio.hotel_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.poroflio.hotel_service.dto.RoomRequestDTO;
import com.poroflio.hotel_service.dto.RoomResponseDTO;
import com.poroflio.hotel_service.entity.RoomType;
import com.poroflio.hotel_service.service.room.IRoomService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/hotels/rooms")
public class RoomController {

    private final IRoomService roomService;

    public RoomController(IRoomService roomService) {
        this.roomService = roomService;
    }

    // -------------------------------------------------
    // CREATE
    // -------------------------------------------------
    @PostMapping
    public ResponseEntity<RoomResponseDTO> createRoom(@Valid @RequestBody RoomRequestDTO dto) {
        return ResponseEntity
                .status(201)
                .body(roomService.createRoom(dto));
    }

    // -------------------------------------------------
    // GET BY ID
    // -------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponseDTO> getRoomById(@PathVariable String id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    // -------------------------------------------------
    // GET ROOMS BY HOTEL ID
    // -------------------------------------------------
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<RoomResponseDTO>> getRoomsByHotel(@PathVariable String hotelId) {
        return ResponseEntity.ok(roomService.getRoomsByHotelId(hotelId));
    }

    @GetMapping("/hotel/{hotelId}/available")
    public ResponseEntity<List<RoomResponseDTO>> getAvailableRoomsByHotelId(@PathVariable String hotelId) {
        return ResponseEntity.ok(roomService.getAvailableRoomsByHotelId(hotelId));
    }

    // -------------------------------------------------
    // GET ROOMS BY TYPE
    // -------------------------------------------------
    @GetMapping("/type/{type}")
    public ResponseEntity<List<RoomResponseDTO>> getRoomsByRoomType(@PathVariable RoomType type) {
        return ResponseEntity.ok(roomService.getRoomsByRoomType(type));
    }

    // -------------------------------------------------
    // UPDATE
    // -------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<RoomResponseDTO> updateRoom(
            @PathVariable String id,
            @Valid @RequestBody RoomRequestDTO dto) {

        return ResponseEntity.ok(roomService.updateRoom(id, dto));
    }

    public ResponseEntity<RoomResponseDTO> updateRoomPatch(
            @PathVariable String id,
            @RequestBody RoomRequestDTO dto) {

        return ResponseEntity.ok(roomService.updateRoom(id, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RoomResponseDTO> patchRoom(
            @PathVariable String id,
            @RequestBody RoomRequestDTO dto) {

        return ResponseEntity.ok(roomService.patchRoom(id, dto));
    }

    // -------------------------------------------------
    // DELETE
    // -------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable String id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}
