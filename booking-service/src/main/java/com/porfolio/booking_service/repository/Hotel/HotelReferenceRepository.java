package com.porfolio.booking_service.repository.Hotel;

import org.springframework.data.jpa.repository.JpaRepository;

import com.porfolio.booking_service.entity.HotelReference.Hotel.HotelReference;

public interface HotelReferenceRepository extends JpaRepository<HotelReference, String> {

}
