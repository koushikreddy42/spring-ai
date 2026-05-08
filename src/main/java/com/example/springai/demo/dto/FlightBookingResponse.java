package com.example.springai.demo.dto;

import com.example.springai.demo.entity.BookingStatus;

import java.time.Instant;

public record FlightBookingResponse(
        Long id,
        String destination,
        BookingStatus bookingStatus,
        Instant departureTime
) {
}
