package com.springAndBoot3.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.springAndBoot3.Entity.BookRoomRequest;
import com.springAndBoot3.Entity.Booking;
import com.springAndBoot3.Entity.Room;
import com.springAndBoot3.enums.RoomType;

public interface BookingService {
	List<Booking> getAllBookings();
    Booking getBookingById(Long id);
    Booking createBooking(Booking booking);
    Booking updateBooking(Long id, Booking booking);
	List<Room> findAvailableRoomsByTypeAndDateRange(RoomType roomType, LocalDate checkInDate, LocalDate checkOutDate);
	ResponseEntity<Object> bookRoom(BookRoomRequest bookRoomRequest);
}
