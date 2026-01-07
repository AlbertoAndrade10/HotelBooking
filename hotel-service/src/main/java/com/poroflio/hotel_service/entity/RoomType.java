package com.poroflio.hotel_service.entity;

import java.math.BigDecimal;

public enum RoomType {
    STANDARD(new BigDecimal("50.00")),
    DELUXE(new BigDecimal("80.00")),
    SUITE(new BigDecimal("120.00"));

    private final BigDecimal pricePerNight;

    RoomType(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public BigDecimal getPricePerNight() {
        return this.pricePerNight;
    }
}
