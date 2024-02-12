package com.springAndBoot3.Entity;

import java.time.LocalDate;

public class AvailabilityRequest {
	private String roomType;
    private LocalDate checkInDate; 
    private LocalDate checkOutDate;
	public String getRoomType() {
		return roomType;
	}
	public void setRoomType(String roomType) {
		this.roomType = roomType;
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
	public AvailabilityRequest(String roomType, LocalDate checkInDate, LocalDate checkOutDate) {
		super();
		this.roomType = roomType;
		this.checkInDate = checkInDate;
		this.checkOutDate = checkOutDate;
	}
	public AvailabilityRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
    
    
}
