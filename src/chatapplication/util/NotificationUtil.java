/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.util;

import chatapplication.Response.Response;
import chatapplication.entity.Room;
import chatapplication.entity.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author VuongKM
 */
public class NotificationUtil {

    public static void noticeUserInRoom(Room room, Response response) {
        // Notice all people in rooms
        ArrayList<User> roomUsers = RoomUtil.getAllUserInRoom(room);
        for (User tmpUser : roomUsers) {
            try {
                tmpUser.out.writeObject(response);
            } catch (IOException ex) {
                Logger.getLogger(RoomUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
