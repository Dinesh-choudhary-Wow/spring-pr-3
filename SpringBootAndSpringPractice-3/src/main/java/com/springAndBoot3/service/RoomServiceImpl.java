package com.springAndBoot3.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springAndBoot3.Entity.Room;
import com.springAndBoot3.dao.RoomDao;

@Service
public class RoomServiceImpl implements RoomService {

	@Autowired
    private RoomDao roomDao;

    @Override
    public List<Room> getAllRooms() {
        return roomDao.findAll();
    }

    @Override
    public Room getRoomById(Long id) {
        return roomDao.findById(id).orElse(null);
    }

    @Override
    public Room createRoom(Room room) {
        return roomDao.save(room);
    }

    @Override
    public Room updateRoom(Long id, Room room) {
        if (!roomDao.existsById(id)) {
            return null;
        }
        room.setRoomId(id);
        return roomDao.save(room);
    }

}
