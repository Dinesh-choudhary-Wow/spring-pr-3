package com.springAndBoot3.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springAndBoot3.Entity.Booking;

public interface BookingDao extends JpaRepository<Booking, Long> {

	@Query("SELECT b.room.roomId FROM Booking b WHERE (b.checkInDate BETWEEN :checkInDate AND :checkOutDate) OR (b.checkOutDate BETWEEN :checkInDate AND :checkOutDate)")
    List<Long> findBookedRoomIdsInRange(@Param("checkInDate") LocalDate checkInDate, @Param("checkOutDate") LocalDate checkOutDate);
	
	@Query("SELECT b FROM Booking b WHERE b.room.id = ?1 AND "
            + "((b.checkInDate BETWEEN ?2 AND ?3) OR (b.checkOutDate BETWEEN ?2 AND ?3))")
    List<Booking> findConflictingBookings(Long roomId, LocalDate checkInDate, LocalDate checkOutDate);
}