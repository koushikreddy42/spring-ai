package com.example.springai.demo.tool;

import com.example.springai.demo.dto.FlightBookingResponse;
import com.example.springai.demo.entity.BookingStatus;
import com.example.springai.demo.entity.FlightBooking;
import com.example.springai.demo.service.FlightBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightBookingTools {
    private final FlightBookingService flightBookingService;
    @Tool(description = "to create a flight booking with the given parameters")
    public FlightBookingResponse createBooking(
            @ToolParam(description = "the user for which we have to create the flight booking")
            String userId,
            @ToolParam(description = "the destination of the flight")
            String destination,
            @ToolParam(description = "the departure time of the flight in format 'yyyy-MM-dd'")
            String departureTime){
        // Parse the date string to Instant
        LocalDate date = LocalDate.parse(departureTime);
        Instant departureInstant = date.atStartOfDay(ZoneId.systemDefault()).toInstant();
        var flightBooking = flightBookingService.createBooking(userId, destination, departureInstant);
        return new FlightBookingResponse(
                flightBooking.getId(),
                flightBooking.getDestination(),
                flightBooking.getBookingStatus(),
                flightBooking.getDepartureTime()
        );
    }

    @Tool(description = "get all the flight bookings of the given user")
    public List<FlightBookingResponse> listBookings(
            @ToolParam(description = "the user for which we have to get the flight bookings")
            String userId){
        var flightBookings = flightBookingService.listBookings(userId);
        return flightBookings.stream().map(
                flightBooking -> new FlightBookingResponse(
                        flightBooking.getId(),
                        flightBooking.getDestination(),
                        flightBooking.getBookingStatus(),
                        flightBooking.getDepartureTime()
                )
        ).collect(Collectors.toList());
    }

    @Tool(description = "update the status of a booking")
    public FlightBookingResponse updateBookingStatus(
            @ToolParam(description = "the id of the booking")
            Long bookingId,
            @ToolParam(description = "the id of the user")
            String userId,
            @ToolParam(description = "the new status of the booking")
            BookingStatus newStatus){
        var flightBooking = flightBookingService.updateBookingStatus(bookingId, userId, newStatus);
        return new FlightBookingResponse(
                flightBooking.getId(),
                flightBooking.getDestination(),
                flightBooking.getBookingStatus(),
                flightBooking.getDepartureTime()
        );
    }
}
