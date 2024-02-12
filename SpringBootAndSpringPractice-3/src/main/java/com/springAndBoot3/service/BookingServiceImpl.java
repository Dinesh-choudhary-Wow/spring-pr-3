package com.springAndBoot3.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springAndBoot3.Entity.BookRoomRequest;
import com.springAndBoot3.Entity.Booking;
import com.springAndBoot3.Entity.Customer;
import com.springAndBoot3.Entity.Room;
import com.springAndBoot3.Entity.RoomType;
import com.springAndBoot3.dao.BookingDao;
import com.springAndBoot3.dao.CustomerDao;
import com.springAndBoot3.dao.RoomDao;
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

	public Booking bookRoom(BookRoomRequest bookRoomRequest) throws RoomTypeUnavailableException {
        Long roomId = bookRoomRequest.getRoomId();
        LocalDate checkInDate = bookRoomRequest.getCheckInDate();
        LocalDate checkOutDate = bookRoomRequest.getCheckOutDate();
        String firstName = bookRoomRequest.getFirstName();
        String lastName = bookRoomRequest.getLastName();
        String email = bookRoomRequest.getEmail();
        String phone = bookRoomRequest.getPhone();

        Room room = roomDao.findById(roomId)
            .orElseThrow(() -> new RoomTypeUnavailableException("Room not found"));

        // Validate room availability for the given dates
        List<Booking> conflictingBookings = bookingDao.findConflictingBookings(roomId, checkInDate, checkOutDate);
        if (!conflictingBookings.isEmpty()) {
            throw new RoomTypeUnavailableException("Room is not available for the specified dates.");
        }

        // Create customer entity
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setPhone(phone);
        // Save customer to the database
        customer = customerDao.save(customer);

        // Create booking entity
        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setCheckInDate(checkInDate);
        booking.setCheckOutDate(checkOutDate);
        booking.setCustomer(customer); // Associate customer with booking

        // Save booking to the database
        return bookingDao.save(booking);
    }
}
