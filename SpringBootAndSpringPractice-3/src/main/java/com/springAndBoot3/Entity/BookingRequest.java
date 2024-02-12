package com.springAndBoot3.Entity;

import java.time.LocalDate;

public class BookingRequest {

	private Long roomId;
    private Long customerId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private BookingStatus status;
	public Long getRoomId() {
		return roomId;
	}
	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public LocalDate getCheckInDate() {
		return checkInDate;
	}
	public void setCheckInDate(LocalDate checkInDate) {
		this.checkInDate = checkInDate;
	}
	public LocalDate getCheckOutDate() {
		return checkOutDate;
	}
	public void setCheckOutDate(LocalDate checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public BookingStatus getStatus() {
		return status;
	}
	public void setStatus(BookingStatus status) {
		this.status = status;
	}
	public BookingRequest(Long roomId, Long customerId, LocalDate checkInDate, LocalDate checkOutDate,
			BookingStatus status) {
		super();
		this.roomId = roomId;
		this.customerId = customerId;
		this.checkInDate = checkInDate;
		this.checkOutDate = checkOutDate;
		this.status = status;
	}
    
    
}
