package com.example.springai.demo.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Builder
@Data
public class FlightBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String userId;

    Instant departureTime;

    @Enumerated(EnumType.STRING)
    BookingStatus bookingStatus;

    String destination;

    @CreationTimestamp
    Instant bookedAt;
}
