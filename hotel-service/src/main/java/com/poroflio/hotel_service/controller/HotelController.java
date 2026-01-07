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

import com.poroflio.hotel_service.dto.HotelRequestDTO;
import com.poroflio.hotel_service.dto.HotelResponseDTO;
import com.poroflio.hotel_service.service.hotel.IHotelService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {
    private final IHotelService hotelService;

    public HotelController(IHotelService hotelService) {
        this.hotelService = hotelService;
    }

    // -------------------------
    // GET
    // -------------------------

    @GetMapping
    public ResponseEntity<List<HotelResponseDTO>> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelResponseDTO> getHotelById(@PathVariable String id) {
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<HotelResponseDTO> getHotelByName(@PathVariable String name) {
        return ResponseEntity.ok(hotelService.getHotelByName(name));
    }

    // -------------------------
    // POST
    // -------------------------

    @PostMapping
    public ResponseEntity<HotelResponseDTO> createHotel(@Valid @RequestBody HotelRequestDTO request) {
        return ResponseEntity.ok(hotelService.createHotel(request));
    }

    // -------------------------
    // PUT
    // -------------------------

    @PutMapping("/{hotelId}")
    public ResponseEntity<HotelResponseDTO> updateHotel(
            @PathVariable String hotelId,
            @Valid @RequestBody HotelRequestDTO request) {

        return ResponseEntity.ok(hotelService.updateHotel(hotelId, request));
    }

    @PatchMapping("/{hotelId}")
    public ResponseEntity<HotelResponseDTO> updateHotelPatch(
            @PathVariable String hotelId,
            @RequestBody HotelRequestDTO request) {

        return ResponseEntity.ok(hotelService.updateHotelPartial(hotelId, request));
    }

    // -------------------------
    // DELETE
    // -------------------------
    @DeleteMapping("/{hotelId}")
    public ResponseEntity<Void> deleteHotel(@PathVariable String hotelId) {
        hotelService.deleteHotel(hotelId);
        return ResponseEntity.noContent().build();
    }

}
