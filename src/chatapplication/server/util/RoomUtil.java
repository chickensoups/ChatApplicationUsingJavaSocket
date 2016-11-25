/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.server.util;

import chatapplication.response.Response;
import chatapplication.command.CreateRoom;
import chatapplication.entity.Room;
import chatapplication.entity.User;
import chatapplication.execute.Server;
import chatapplication.execute.Server;
import chatapplication.util.Config;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author VuongKM
 */
public class RoomUtil {

    public static HashSet<Room> getListRoom() {
        return Server.rooms;
    }

    public static Room createRoom(CreateRoom createRoom) {
        Room newRoom = new Room(createRoom);
        Server.rooms.add(newRoom);
        return newRoom;
    }

    public static boolean isRoomFull(Room room) {
        int roomUserCount = 0;
        for (User user : Server.users) {
            if (user.currentRoom != null && user.currentRoom.name.equals(room.name)) {
                roomUserCount++;
            }
        }
        return (roomUserCount >= Config.maxUserPerRoom);
    }

    public static Room findRoom(Room room) {
        for (Room tmpRoom : Server.rooms) {
            if (tmpRoom.name.equals(room.name)) {
                return tmpRoom;
            }
        }
        return null;
    }

    public static Room findRoom(String roomName) {
        for (Room tmpRoom : Server.rooms) {
            if (tmpRoom.name.equals(roomName)) {
                return tmpRoom;
            }
        }
        return null;
    }

    public static boolean isRoomExisted(String roomName) {
        return findRoom(roomName) != null;
    }

    public static ArrayList<User> getAllUserInRoom(Room room) {
        ArrayList<User> roomUsers;
        roomUsers = new ArrayList<>();
        for (User user : Server.users) {
            if (user.currentRoom != null && user.currentRoom.name.equals(room.name)) {
                roomUsers.add(user);
            }
        }
        return roomUsers;
    }
}
