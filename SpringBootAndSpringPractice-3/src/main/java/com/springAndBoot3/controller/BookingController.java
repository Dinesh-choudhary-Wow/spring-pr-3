package com.springAndBoot3.controller;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.springAndBoot3.dao.BookingDao;
import com.springAndBoot3.dao.CustomerDao;
import com.springAndBoot3.dao.RoomDao;
import com.springAndBoot3.exception.RoomTypeUnavailableException;
import com.springAndBoot3.service.BookingService;

@RestController
@RequestMapping("/booking")
public class BookingController {
	
	@Autowired
	private BookingService bookingService;

	@Autowired
	private RoomDao roomDao;
	@Autowired
	private BookingDao bookingDao;

	@Autowired
	private CustomerDao customerDao;

	@GetMapping
	public List<Booking> getAllBookings() {
		return bookingService.getAllBookings();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
		Booking booking = bookingService.getBookingById(id);
		if (booking != null) {
			return ResponseEntity.ok(booking);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest bookingRequest) {
		// Fetch existing room and customer entities from the database
		Room room = roomDao.findById(bookingRequest.getRoomId()).orElse(null);
		Customer customer = customerDao.findById(bookingRequest.getCustomerId()).orElse(null);

		if (room == null || customer == null) {
			// Handle case where room or customer does not exist
			return ResponseEntity.badRequest().build();
		}
		
		// Calculate duration of stay
        Duration duration = Duration.between(bookingRequest.getCheckInDate().atStartOfDay(), bookingRequest.getCheckOutDate().atStartOfDay());

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
	public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody Booking booking) {
		Booking updatedBooking = bookingService.updateBooking(id, booking);
		if (updatedBooking != null) {
			return ResponseEntity.ok(updatedBooking);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	
	@PostMapping("/book")
	public Booking bookRoom(@RequestBody BookRoomRequest bookRoomRequest) throws RoomTypeUnavailableException {
		System.out.println(bookRoomRequest.getRoomId());
		Room room = roomDao.findById(bookRoomRequest.getRoomId()).orElseThrow(() -> new RoomTypeUnavailableException("Room not found"));

		
		//Validate room availability for the given dates
		List<Booking> conflictingBookings = bookingDao.findConflictingBookings(bookRoomRequest.getRoomId(), bookRoomRequest.getCheckInDate(), bookRoomRequest.getCheckOutDate());
		if (!conflictingBookings.isEmpty()) {
			throw new RoomTypeUnavailableException("Room is not available for the specified dates.");
		}

		// Calculate duration of stay
        Duration duration = Duration.between(bookRoomRequest.getCheckInDate().atStartOfDay(), bookRoomRequest.getCheckOutDate().atStartOfDay());

        // Convert pricePerDay to BigDecimal
        BigDecimal pricePerDayBigDecimal = BigDecimal.valueOf(room.getPricePerDay());

        // Calculate total price
        BigDecimal totalPrice = pricePerDayBigDecimal.multiply(BigDecimal.valueOf(duration.toDays()));

		//Create customer entity
		Customer customer = new Customer();
		customer.setFirstName(bookRoomRequest.getFirstName());
		customer.setLastName(bookRoomRequest.getLastName());
		customer.setEmail(bookRoomRequest.getEmail());
		customer.setPhone(bookRoomRequest.getPhone());
		customer.setLocation(bookRoomRequest.getLocation());
		// Save customer to the database
		customer = customerDao.save(customer);

		//Create booking entity
		Booking booking = new Booking();
		booking.setRoom(room);
		booking.setCheckInDate(bookRoomRequest.getCheckInDate());
		booking.setCheckOutDate(bookRoomRequest.getCheckOutDate());
		booking.setCustomer(customer); // Associate customer with booking
		booking.setStatus(bookRoomRequest.getStatus());
		booking.setTotalPrice(totalPrice);
		//Save booking to the database
		return bookingDao.save(booking);
	}
}
