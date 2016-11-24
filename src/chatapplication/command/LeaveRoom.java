/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.command;

import chatapplication.entity.Room;
import chatapplication.entity.User;
import chatapplication.util.Config;

/**
 *
 * @author VuongKM
 */
public class LeaveRoom extends Command {

    public Room room;

    public LeaveRoom(User creator, Room room) {
        this.name = Config.Command.LEAVE_ROOM.toString();
        this.creator = creator;
        this.room = room;
    }
}
