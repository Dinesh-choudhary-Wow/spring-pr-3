package com.springAndBoot3.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.springAndBoot3.Entity.BookRoomRequest;
import com.springAndBoot3.Entity.Booking;
import com.springAndBoot3.Entity.Customer;
import com.springAndBoot3.Entity.Room;
import com.springAndBoot3.dao.BookingDao;
import com.springAndBoot3.dao.CustomerDao;
import com.springAndBoot3.dao.RoomDao;
import com.springAndBoot3.enums.BookingStatus;
import com.springAndBoot3.enums.RoomType;
import com.springAndBoot3.exception.RoomTypeUnavailableException;

@Service
public class BookingServiceImpl implements BookingService {
	@Autowired
	private BookingDao bookingDao;
	@Autowired
	private RoomDao roomDao;
	@Autowired
	private CustomerDao customerDao;

	@Override
	public List<Booking> getAllBookings() {
		return bookingDao.findAll();
	}

	@Override
	public Booking getBookingById(Long id) {
		return bookingDao.findById(id).orElse(null);
	}

	@Override
	public Booking createBooking(Booking booking) {
		return bookingDao.save(booking);
	}

	@Override
	public List<Room> findAvailableRoomsByTypeAndDateRange(RoomType roomType, LocalDate checkInDate,
			LocalDate checkOutDate) {
		List<Room> availableRooms = new ArrayList<>();

		List<Long> bookedRoomIds = bookingDao.findBookedRoomIdsInRange(checkInDate, checkOutDate);

		// Find available rooms of the specified type
		List<Room> rooms = roomDao.findByRoomType(roomType);
		for (Room room : rooms) {
			if (!bookedRoomIds.contains(room.getRoomId())) {
				availableRooms.add(room);
			}
		}

		if (availableRooms.isEmpty()) {
			throw new RoomTypeUnavailableException(
					"No rooms of type " + roomType + " available for the specified dates.");
		}

		return availableRooms;
	}

	@Override
	public Booking updateBooking(Long id, Booking booking) {
		if (!bookingDao.existsById(id)) {
			return null;
		}
		booking.setBookingId(id);
		return bookingDao.save(booking);
	}

	public ResponseEntity<Object> bookRoom(BookRoomRequest bookRoomRequest) throws RoomTypeUnavailableException {
		Long roomId = bookRoomRequest.getRoomId();
		try {
			if (roomId == null) {
				throw new IllegalArgumentException("RoomId Cannot be Empty. It is required for the booking of room.");
			}
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		}
		try {
			Room room = roomDao.findById(roomId).orElseThrow(() -> new RoomTypeUnavailableException("Room not found"));

			// Validate Check-in and Check-out Dates
			LocalDate checkInDate = bookRoomRequest.getCheckInDate();
			LocalDate checkOutDate = bookRoomRequest.getCheckOutDate();
			try {
				if (checkInDate == null || checkOutDate == null) {
					throw new IllegalArgumentException("Check-in date and Check-out-date cannot be empty.");
				}
				if (checkInDate.isAfter(checkOutDate)) {
					throw new IllegalArgumentException("Check-in date must be before check-out date.");
				}
			} catch (IllegalArgumentException ex) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
			} catch (RoomTypeUnavailableException ex) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
			}

			// Validate Date Range
			LocalDate currentDate = LocalDate.now();
			try {
				if (checkInDate.isBefore(currentDate) || checkOutDate.isBefore(currentDate)) {
					throw new IllegalArgumentException("Check-in and check-out dates cannot be in the past.");
				}
			} catch (IllegalArgumentException ex) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
			}

			// Validate Customer Information
			String firstName = bookRoomRequest.getFirstName();
			String lastName = bookRoomRequest.getLastName();
			String email = bookRoomRequest.getEmail();
			String phone = bookRoomRequest.getPhone();
			BookingStatus status = bookRoomRequest.getStatus();
			try {
				if (firstName == null || lastName == null || email == null || phone == null || status == null) {
					throw new IllegalArgumentException("Customer information cannot be null.");
				}
			} catch (IllegalArgumentException ex) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
			}

			// Validate room availability for the given dates
			List<Booking> conflictingBookings = bookingDao.findConflictingBookings(roomId, checkInDate, checkOutDate);
			try {
				if (!conflictingBookings.isEmpty()) {
					throw new RoomTypeUnavailableException("Room is not available for the specified dates.");
				}
			} catch (IllegalArgumentException ex) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
			}

			// Calculate duration of stay
			Duration duration = Duration.between(bookRoomRequest.getCheckInDate().atStartOfDay(),
					bookRoomRequest.getCheckOutDate().atStartOfDay());

			// Convert pricePerDay to BigDecimal
			BigDecimal pricePerDayBigDecimal = BigDecimal.valueOf(room.getPricePerDay());

			// Calculate total price
			BigDecimal totalPrice = pricePerDayBigDecimal.multiply(BigDecimal.valueOf(duration.toDays()));

			// Create customer entity
			Customer customer = new Customer();
			customer.setFirstName(bookRoomRequest.getFirstName());
			customer.setLastName(bookRoomRequest.getLastName());
			customer.setEmail(bookRoomRequest.getEmail());
			customer.setPhone(bookRoomRequest.getPhone());
			customer.setLocation(bookRoomRequest.getLocation());
			// Save customer to the database
			customer = customerDao.save(customer);

			// Create booking entity
			Booking booking = new Booking();
			booking.setRoom(room);
			booking.setCheckInDate(bookRoomRequest.getCheckInDate());
			booking.setCheckOutDate(bookRoomRequest.getCheckOutDate());
			booking.setCustomer(customer); // Associate customer with booking
			booking.setStatus(bookRoomRequest.getStatus());
			booking.setTotalPrice(totalPrice);
			// Save booking to the database

			bookingDao.save(booking);
		} catch (RoomTypeUnavailableException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body("Booking Saved to the Database");
	}
}
