package com.springAndBoot3.service;

import java.util.List;

import com.springAndBoot3.Entity.Room;

public interface RoomService {
	List<Room> getAllRooms();
    Room getRoomById(Long id);
    Room createRoom(Room room);
    Room updateRoom(Long id, Room room);
}
