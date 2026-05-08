package com.example.springai.demo.service;

import com.example.springai.demo.dto.FlightBookingResponse;
import com.example.springai.demo.entity.BookingStatus;
import com.example.springai.demo.entity.FlightBooking;
import com.example.springai.demo.repository.FlightBookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FlightBookingService {
    private final FlightBookingRepository flightBookingRepository;

    public FlightBooking createBooking(String userId, String destination, Instant departureTime){
        boolean isBookingExists = flightBookingRepository.existsByUserIdAndDestinationAndDepartureTime(userId, destination, departureTime);
        if(isBookingExists){
            throw new IllegalArgumentException("you already have a flight booking for the "+ destination+ " on that date.");
        }
        FlightBooking flightBooking = FlightBooking.builder()
                .userId(userId)
                .destination(destination)
                .departureTime(departureTime)
                .bookingStatus(BookingStatus.BOOKED)
                .build();

        return flightBookingRepository.save(flightBooking);
    }

    public List<FlightBooking> listBookings(String userId){
        return flightBookingRepository.findByUserIdOrderByDepartureTimeDesc(userId);
    }

    public FlightBooking updateBookingStatus(Long bookingId, String userId, BookingStatus newStatus){
        FlightBooking flightBooking = flightBookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        String ownerId = flightBooking.getUserId();
        if(!Objects.equals(ownerId, userId)){
            throw new IllegalArgumentException("Only owner of the booking can edit the status");
        }
        flightBooking.setBookingStatus(newStatus);
        return flightBookingRepository.save(flightBooking);
    }
}
