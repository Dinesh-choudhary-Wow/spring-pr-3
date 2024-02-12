package com.springAndBoot3.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springAndBoot3.Entity.Room;
import com.springAndBoot3.enums.RoomType;

public interface RoomDao extends JpaRepository<Room, Long> {

	List<Room> findByRoomType(RoomType roomType);

}
