package com.springAndBoot3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springAndBoot3.Entity.AvailabilityRequest;
import com.springAndBoot3.Entity.Room;
import com.springAndBoot3.enums.RoomType;
import com.springAndBoot3.exception.RoomTypeUnavailableException;
import com.springAndBoot3.service.BookingService;

@RestController
@RequestMapping("/availability")
public class AvailabilityController {

	@Autowired
	private BookingService bookingService;

	@PostMapping("/check")
	public ResponseEntity<Object> checkRoomAvailability(@Validated @RequestBody AvailabilityRequest availabilityRequest) {
		try {
			// Call the service method to check room availability
			List<Room> availableRooms = bookingService.findAvailableRoomsByTypeAndDateRange(
					RoomType.valueOf(availabilityRequest.getRoomType()), availabilityRequest.getCheckInDate(),
					availabilityRequest.getCheckOutDate());

			// Return the list of available rooms
			return ResponseEntity.ok(availableRooms);
		} catch (RoomTypeUnavailableException ex) {
			String errorMessage = "Room of type " + availabilityRequest.getRoomType()
					+ " is not available for the specified dates.";
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("RoomType field is required for the search");
		}
	}
}
