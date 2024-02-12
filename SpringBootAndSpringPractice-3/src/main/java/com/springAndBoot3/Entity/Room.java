package com.springAndBoot3.Entity;

import com.springAndBoot3.enums.RoomType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "rooms")
public class Room {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    
    private String hotelName;
    
    @Enumerated(EnumType.STRING)
    private RoomType roomType;
    
    private int pricePerDay;
    
    private int maxOccupancy;

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}

	public int getPricePerDay() {
		return pricePerDay;
	}

	public void setPricePerDay(int pricePerDay) {
		this.pricePerDay = pricePerDay;
	}

	public int getMaxOccupancy() {
		return maxOccupancy;
	}

	public void setMaxOccupancy(int maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}

	public Room(Long roomId, String hotelName, RoomType roomType, int pricePerDay, int maxOccupancy) {
		super();
		this.roomId = roomId;
		this.hotelName = hotelName;
		this.roomType = roomType;
		this.pricePerDay = pricePerDay;
		this.maxOccupancy = maxOccupancy;
	}

	public Room() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
	
}
