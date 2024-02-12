package com.springAndBoot3.Entity;

import java.time.LocalDate;

public class BookRoomRequest {
	private Long roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String location;
    private BookingStatus status;
    
    
	public BookingStatus getStatus() {
		return status;
	}
	public void setStatus(BookingStatus status) {
		this.status = status;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Long getRoomId() {
		return roomId;
	}
	public void setRoomId(Long roomId) {
		this.roomId = roomId;
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
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public BookRoomRequest(Long roomId, LocalDate checkInDate, LocalDate checkOutDate, String firstName,
			String lastName, String email, BookingStatus status, String phone, String location) {
		super();
		this.roomId = roomId;
		this.checkInDate = checkInDate;
		this.checkOutDate = checkOutDate;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.status = status;
		this.location = location;
	}
	
	
}
