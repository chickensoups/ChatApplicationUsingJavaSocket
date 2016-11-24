/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.util;

import chatapplication.command.JoinRoom;
import chatapplication.command.LeaveRoom;
import chatapplication.command.Login;
import chatapplication.command.Logout;
import chatapplication.entity.Room;
import chatapplication.entity.User;
import chatapplication.server.Server;
import java.util.ArrayList;

/**
 *
 * @author VuongKM
 */
public class UserUtil {

    public static void joinRoom(JoinRoom joinRoom) {
        User user = findUser(joinRoom.creator);
        Room room = RoomUtil.findRoom(joinRoom.room);
        user.currentRoom = room;
        room.userCount++;
    }

    public static void leaveRoom(LeaveRoom leaveRoom) {
        User user = findUser(leaveRoom.creator);
        Room room = RoomUtil.findRoom(user.currentRoom);
        room.userCount--;
        user.currentRoom = null;
        // Notice all people remain in rooms
    }

    public static User logUserIn(Login login) {
        for (User tmpUser : Server.users) {
            if (tmpUser.id.equals(login.id)) {
                tmpUser.name = login.userName;
                return null;
            }
        }
        User user = new User(login.id, login.userName);
        Server.users.add(user);
        return user;
    }

    public static void logUserOut(Logout logout) {
        Server.users.remove(findUser(logout.creator));
    }

    public static User findUser(User user) {
        for (User tmpUser : Server.users) {
            if (tmpUser.name.equals(user.name)) {
                return tmpUser;
            }
        }
        return null;
    }

    public static User findUser(String username) {
        for (User tmpUser : Server.users) {
            if (tmpUser.name.equals(username)) {
                return tmpUser;
            }
        }
        return null;
    }

    public static boolean isUserExisted(String username) {
        return findUser(username) != null;
    }
}
