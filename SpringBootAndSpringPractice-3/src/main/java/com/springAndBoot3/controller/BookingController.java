package com.springAndBoot3.controller;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springAndBoot3.Entity.BookRoomRequest;
import com.springAndBoot3.Entity.Booking;
import com.springAndBoot3.Entity.BookingRequest;
import com.springAndBoot3.Entity.Customer;
import com.springAndBoot3.Entity.Room;
import com.springAndBoot3.dao.CustomerDao;
import com.springAndBoot3.dao.RoomDao;
import com.springAndBoot3.service.BookingService;

@RestController
@RequestMapping("/booking")
public class BookingController {

	@Autowired
	private BookingService bookingService;

	@Autowired
	private RoomDao roomDao;

	@Autowired
	private CustomerDao customerDao;

	@GetMapping
	public List<Booking> getAllBookings() {
		return bookingService.getAllBookings();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getBookingById(@PathVariable Long id) {
		Booking booking = bookingService.getBookingById(id);
		try {
			if (booking != null) {
				return ResponseEntity.ok(booking);
			} else {
				throw new IllegalArgumentException("Room is not available for the specified dates.");
			}			
		}catch (IllegalArgumentException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		}
	}

	@PostMapping
	public ResponseEntity<Object> createBooking(@Validated @RequestBody BookingRequest bookingRequest) {
		// Fetch existing room and customer entities from the database
		Room room = roomDao.findById(bookingRequest.getRoomId()).orElse(null);
		Customer customer = customerDao.findById(bookingRequest.getCustomerId()).orElse(null);

		try {
			if (room == null || customer == null) {
				// Handle case where room or customer does not exist
				throw new IllegalArgumentException("Room is not available for the specified dates.");
			}
		}catch (IllegalArgumentException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		}

		// Calculate duration of stay
		Duration duration = Duration.between(bookingRequest.getCheckInDate().atStartOfDay(),
				bookingRequest.getCheckOutDate().atStartOfDay());

		// Convert pricePerDay to BigDecimal
		BigDecimal pricePerDayBigDecimal = BigDecimal.valueOf(room.getPricePerDay());

		// Calculate total price
		BigDecimal totalPrice = pricePerDayBigDecimal.multiply(BigDecimal.valueOf(duration.toDays()));

		// Create a new booking entity
		Booking booking = new Booking();
		booking.setRoom(room);
		booking.setCustomer(customer);
		booking.setCheckInDate(bookingRequest.getCheckInDate());
		booking.setCheckOutDate(bookingRequest.getCheckOutDate());
		booking.setTotalPrice(totalPrice);
		booking.setStatus(bookingRequest.getStatus());
		// Save the booking entity
		Booking createdBooking = bookingService.createBooking(booking);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Object> updateBooking(@PathVariable Long id, @RequestBody Booking booking) {
		Booking updatedBooking = bookingService.updateBooking(id, booking);
		try {
			if (updatedBooking != null) {
				return ResponseEntity.ok(updatedBooking);
			} else {
				throw new IllegalArgumentException("Room is not available for the specified dates.");
			}
		}catch (IllegalArgumentException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		}		
	}

	
	@PostMapping("/book")
	public ResponseEntity<Object> bookRoom(@RequestBody BookRoomRequest bookRoomRequest) {
		try {
			return bookingService.bookRoom(bookRoomRequest);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
		}
	}
}
