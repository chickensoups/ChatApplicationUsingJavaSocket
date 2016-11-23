/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.util;

import chatapplication.command.JoinRoom;
import chatapplication.command.LeaveRoom;
import chatapplication.command.Login;
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
    }
    
    public static void leaveRoom(LeaveRoom leaveRoom) {
        findUser(leaveRoom.creator).currentRoom = null;
        
        // Notice all people remain in rooms
    }

    public static void logUserIn(Login login) {
        Server.users.add(new User(login.userName));
        
        // Notice to logged user
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
