package com.poroflio.hotel_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poroflio.hotel_service.entity.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, String> {
    Optional<Hotel> findByName(String name);

    Optional<Hotel> findByEmail(String email);

    boolean existsByName(String name);

    boolean existsByEmail(String email);

}
