package com.example.springai.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
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
